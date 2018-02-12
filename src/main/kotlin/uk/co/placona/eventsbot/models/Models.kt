package uk.co.placona.eventsbot.models

// Events
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

// Tags
data class HawkeyeTagResponse(val count: Int, val _embedded: EmbeddedTag)

open class Tag(
        open val id: Int,
        open val name: String
)

data class EmbeddedTag(val tags: List<Tag>)

// Countries
data class HawkeyeCountryResponse(val count: Int, val _embedded: EmbeddedCountry)

open class Country(
        open val id: Int,
        open val name: String
)

data class EmbeddedCountry(val countries: List<Tag>)