package uk.co.placona.eventsbot.utilities

private fun encode(message: String): String {
    return message.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}

fun countrySpell(country: String): String{
    var hawkeyeCountrySpell: String = country

    when(country.toLowerCase()){
        "uk", "england" -> hawkeyeCountrySpell = "United Kingdom"
        "usa", "america" -> hawkeyeCountrySpell = "United States"
    }

    return hawkeyeCountrySpell
}