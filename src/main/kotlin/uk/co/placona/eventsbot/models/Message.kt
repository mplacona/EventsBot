package uk.co.placona.eventsbot.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * A model of a Slack message response.
 *
 * <p>See: https://api.slack.com/docs/messages
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
open class Message(open val text: String, open val responseType: ResponseType)
