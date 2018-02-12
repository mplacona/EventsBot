package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
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

    fun getEventsByCountry(country: String): Observable<HawkeyeEventResponse> {
        return hawkeyeClient.getEventsByCountry(
                LocalDateTime.now().toString(),
                countrySpell(country),
                System.getProperty("HAWKEYE_PASSWORD"))
    }

    @Cacheable(cacheNames = ["persistedTags"])
    fun getTags(): Observable<HawkeyeTagResponse>{
        return hawkeyeClient.getTags(System.getProperty("HAWKEYE_PASSWORD"))
    }
}