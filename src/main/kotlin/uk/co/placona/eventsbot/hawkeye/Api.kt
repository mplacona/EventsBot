package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.placona.eventsbot.models.HawkeyeApiResponse

interface Api {
    // All Green events
    @GET("/api/events?viewModel.includeNoAssignedAttendees=false&viewModel.statusId=3&viewModel.pageSize=1000")
    fun getEvents(
            @Query("viewModel.startDate") startDate: String,
            @Query("viewModel.country") country: String,
            @Query("api_key") apiKey: String)
            : Observable<HawkeyeApiResponse>
}