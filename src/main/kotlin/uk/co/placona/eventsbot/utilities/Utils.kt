package uk.co.placona.eventsbot.utilities

private fun encode(message: String): String {
    return message.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}