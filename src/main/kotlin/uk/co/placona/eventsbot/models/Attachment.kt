package uk.co.placona.eventsbot.models

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * A model of a Slack message response.
 *
 * <p>See: https://api.slack.com/docs/message-attachments
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Attachments(val attachments: ArrayList<Attachment>, val text: String, val responseType: ResponseType)

data class Attachment (val title: String, val text: String, var thumb_url: String = "", var footer: String = "", var footer_icon: String = "", val color: String = "#36a64f")
