package uk.co.placona.eventsbot

import io.reactivex.schedulers.Schedulers
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import uk.co.placona.eventsbot.hawkeye.HawkeyeClient
import uk.co.placona.eventsbot.models.Attachment
import uk.co.placona.eventsbot.models.Attachments
import uk.co.placona.eventsbot.models.Message
import uk.co.placona.eventsbot.models.ResponseType
import uk.co.placona.eventsbot.slack.SlackClient
import uk.co.placona.eventsbot.utilities.UnauthorisedException
import java.text.SimpleDateFormat


@RestController
class SlackController {

    private val log = Logger.getLogger(SlackController::class.simpleName)

    @RequestMapping("/hello", produces = ["application/json"])
    fun hello(
            @ModelAttribute("token") token: String,
            @ModelAttribute("team_id") teamId: String,
            @ModelAttribute("team_domain") teamDomain: String,
            @ModelAttribute("channel_id") channelId: String,
            @ModelAttribute("channel_name") channelName: String,
            @ModelAttribute("user_id") userId: String,
            @ModelAttribute("user_name") userName: String,
            @ModelAttribute("command") command: String,
            @ModelAttribute("text") text: String,
            @ModelAttribute("response_url") responseUrl: String): DeferredResult<Message> {
        val desiredToken = System.getProperty("VERIFICATION_TOKEN")
        val defResult = DeferredResult<Message>()
        val attachments: kotlin.collections.MutableList<Attachment> = java.util.ArrayList()
        val outputFormat = SimpleDateFormat("EEE, dd MMM YY")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

        if (desiredToken != token) {
            throw UnauthorisedException()
        }

        if (text.isEmpty() || text.equals("help", true)) {
            defResult.setResult(Message("I return country events for #mkg-developer-network \n" +
                    "Try typing something like `/events England` to see a list of events.",
                    ResponseType.EPHEMERAL
            ))
            return defResult
        }

        // Return initial message
        defResult.setResult(Message("Checking for events in ${text.capitalize()}", ResponseType.EPHEMERAL))

        // Return follow-up message with events asynchronously
        HawkeyeClient.getEventsAsync(text)
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { t ->

                            // If we have any results for that location
                            if (t.count > 0) {
                                log.info("Got results.")
                                t._embedded.events.mapTo(attachments) {
                                    Attachment(it.name,
                                            "*City:* ${it.metro.city}\n" +
                                                    "*Start:* ${outputFormat.format(inputFormat.parse(it.start))}\n" +
                                                    "*End:* ${outputFormat.format(inputFormat.parse(it.end))}\n" +
                                                    "*Attending:* ${it.attending.map { i -> i.firstName + " " + i.lastName }}"

                                    )
                                }

                                // Update last attachment to contain BOT information
                                val lastAttachment =  attachments[attachments.size-1]
                                lastAttachment.thumb_url = "https://www.twilio.com/docs/static/img/tq.1008.png"
                                lastAttachment.footer_icon = "https://www.twilio.com/docs/static/img/tq.1008.png"
                                lastAttachment.footer = "Built with ❤️ by @mplacona"


                                // Return with all the events
                                SlackClient.responseFollowUp(
                                        responseUrl,
                                        Attachments(attachments as ArrayList<Attachment>, "${t.count} events for *${text.capitalize()}*", ResponseType.EPHEMERAL)
                                )
                                        .subscribeOn(Schedulers.newThread())
                                        .subscribe(
                                                { s: String -> print(s) },
                                                { error -> log.error(error) }
                                        )
                            } else {
                                SlackClient.responseFollowUp(
                                        responseUrl,
                                        Attachments(ArrayList(),"Sorry, no results for *${text.capitalize()}* 💔", ResponseType.EPHEMERAL)
                                )
                                        .subscribe(
                                                { s: String -> print(s) },
                                                { error -> log.error(error) }
                                        )
                            }


                        },
                        { error -> log.error(error) }
                )

        return defResult
    }

    @RequestMapping("/health")
    fun healthy(): String {
        return "Still alive."
    }
}