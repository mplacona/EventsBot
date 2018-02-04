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
        val desiredToken = System.getenv("VERIFICATION_TOKEN")
        val defResult = DeferredResult<Message>()

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

        val list: kotlin.collections.MutableList<Attachment> = java.util.ArrayList()
        list.add(Attachment("something", "title"))
        list.add(Attachment("something else", "another title"))

        // Return initial message
        defResult.setResult(Message("Checking for events in ${text.capitalize()}", ResponseType.EPHEMERAL))

        HawkeyeClient.getEventsAsync(text)
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { t ->

                            // If we have any results for that location
                            if (t.count > 0) {
                                log.info("Got results.")
                                SlackClient.responseFollowUp(
                                        responseUrl,
                                        Attachments(list as ArrayList<Attachment>, "Main Title", ResponseType.EPHEMERAL)
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