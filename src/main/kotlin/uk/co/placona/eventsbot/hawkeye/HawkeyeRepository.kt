package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import org.springframework.stereotype.Repository
import uk.co.placona.eventsbot.EventsBotApp
import uk.co.placona.eventsbot.models.HawkeyeCountryResponse
import uk.co.placona.eventsbot.models.HawkeyeEventResponse
import uk.co.placona.eventsbot.models.HawkeyeTagResponse
import uk.co.placona.eventsbot.utilities.countrySpell
import java.time.LocalDateTime


@Repository
open class HawkeyeRepository {
    companion object {
        private val hawkeyeClient by lazy {
            HawkeyeClient.create()
        }
    }

    fun getEventsByCountryOrTag(text: String): Observable<HawkeyeEventResponse> {
        val isTag = checkIsTag(text)
        return if(isTag != 0){
            hawkeyeClient.getEventsByTag(
                    LocalDateTime.now().toString(),
                    isTag.toString(),
                    System.getProperty("HAWKEYE_PASSWORD"))
        }
        else {
            hawkeyeClient.getEventsByCountry(
                    LocalDateTime.now().toString(),
                    countrySpell(text),
                    System.getProperty("HAWKEYE_PASSWORD"))
        }
    }

    fun getTags(): Observable<HawkeyeTagResponse>{
        return hawkeyeClient.getTags(System.getProperty("HAWKEYE_PASSWORD"))
    }

    fun getCountries(): Observable<HawkeyeCountryResponse>{
        return hawkeyeClient.getCountries(System.getProperty("HAWKEYE_PASSWORD"))
    }

    private fun checkIsTag(text: String): Int{
        val tags = EventsBotApp.tags
        return tags.getOrDefault(text, 0)
    }

    fun checkIsCountry(text: String): Int{
        val countries = EventsBotApp.countries
        return countries.getOrDefault(text, 0)
    }
}