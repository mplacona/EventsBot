package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.placona.eventsbot.models.HawkeyeApiResponse

interface Api {
    // TODO: Change this with the real deal
    @GET("/bins/wmy4t?viewModel.includeNoAssignedAttendees=false&viewModel.statusId=3")
    fun getEvents(
            @Query("viewModel.startDate") startDate: String,
            @Query("viewModel.country") country: String,
            @Query("api_key") apiKey: String)
            : Observable<HawkeyeApiResponse>
}