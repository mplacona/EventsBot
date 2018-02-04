package uk.co.placona.eventsbot.models

import com.fasterxml.jackson.annotation.JsonProperty

enum class ResponseType {
    @JsonProperty("ephemeral")
    EPHEMERAL,

    @JsonProperty("in_channel")
    IN_CHANNEL
}