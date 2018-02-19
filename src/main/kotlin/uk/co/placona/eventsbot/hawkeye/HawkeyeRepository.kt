package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import uk.co.placona.eventsbot.EventsBotApp
import uk.co.placona.eventsbot.models.HawkeyeEventResponse
import uk.co.placona.eventsbot.utilities.countrySpell
import java.time.LocalDateTime

@Repository
open class HawkeyeRepository @Autowired constructor(private val hawkeyeService: HawkeyeService) {

    fun getEventsByCountryOrTag(text: String): Observable<HawkeyeEventResponse> {
        val isTag = checkIsTag(text)
        return if(isTag != 0){
            hawkeyeService.getEventsByTag(
                    LocalDateTime.now().toString(),
                    isTag.toString(),
                    System.getProperty("HAWKEYE_PASSWORD"))
        }
        else {
            hawkeyeService.getEventsByCountry(
                    LocalDateTime.now().toString(),
                    countrySpell(text),
                    System.getProperty("HAWKEYE_PASSWORD"))
        }
    }

    fun getTags(): Observable<Map<String, Int>>{
        return hawkeyeService.getTags(System.getProperty("HAWKEYE_PASSWORD"))
                .subscribeOn(Schedulers.io())
                .map {
                    it._embedded.tags.associateBy( {it.name}, {it.id})
                }
    }

    fun getCountries(): Observable<Map<String, Int>>{
        return hawkeyeService.getCountries(System.getProperty("HAWKEYE_PASSWORD"))
                .subscribeOn(Schedulers.io())
                .map {
                    it._embedded.countries.associateBy( {it.name}, {it.id})
                }
    }

    fun checkIsTag(text: String): Int{
        val tags = EventsBotApp.tags
        return tags.getOrDefault(text, 0)
    }

    fun checkIsCountry(text: String): Int{
        val countries = EventsBotApp.countries
        return countries.getOrDefault(text, 0)
    }
}