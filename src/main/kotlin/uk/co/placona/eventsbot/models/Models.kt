package uk.co.placona.eventsbot.models

data class HawkeyeApiResponse(val count: Int, val _embedded: Embedded)
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

data class Embedded(val events: List<Event>)

data class Metro (val city: String)