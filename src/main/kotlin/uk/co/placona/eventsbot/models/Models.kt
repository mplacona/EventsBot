package uk.co.placona.eventsbot.models

data class HawkeyeEventResponse(val count: Int, val _embedded: EmbeddedEvent)
data class Event(
        val id: Int,
        val name: String,
        val start: String,
        val end: String,
        val attending: List<Attending>,
        val metro: Metro
)
data class Attending(
        val id: String,
        val firstName: String,
        val lastName: String
)

data class EmbeddedEvent(val events: List<Event>)

data class Metro (val city: String)

data class HawkeyeTagResponse(val count: Int, val _embedded: EmbeddedTag)

data class Tag(
        val id: Int,
        val name: String
)

data class EmbeddedTag(val tags: List<Tag>)