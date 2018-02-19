package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import io.reactivex.Single
import org.springframework.cache.annotation.Cacheable
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.placona.eventsbot.models.HawkeyeCountryResponse
import uk.co.placona.eventsbot.models.HawkeyeEventResponse
import uk.co.placona.eventsbot.models.HawkeyeTagResponse

interface HawkeyeService {
    // All Green events
    @GET("/api/events?viewModel.includeNoAssignedAttendees=false&viewModel.statusId=3&viewModel.pageSize=1000&viewModel.sort=start")
    fun getEventsByCountry(
            @Query("viewModel.startDate") startDate: String,
            @Query("viewModel.country") country: String,
            @Query("api_key") apiKey: String)
            : Observable<HawkeyeEventResponse>

    @GET("/api/events?viewModel.includeNoAssignedAttendees=false&viewModel.statusId=3&viewModel.pageSize=1000&viewModel.sort=start")
    fun getEventsByTag(
            @Query("viewModel.startDate") startDate: String,
            @Query("viewModel.eventTagIds") tag: String,
            @Query("api_key") apiKey: String)
            : Observable<HawkeyeEventResponse>

    @GET("/api/tags?pageSize=1000")
    @Cacheable("tags")
    fun getTags(@Query("api_key") apiKey: String) : Observable<HawkeyeTagResponse>

    @GET("/api/countries?pageSize=1000")
    @Cacheable("countries")
    fun getCountries(@Query("api_key") apiKey: String) : Observable<HawkeyeCountryResponse>
}