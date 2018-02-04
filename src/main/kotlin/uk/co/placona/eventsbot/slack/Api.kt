package uk.co.placona.eventsbot.slack

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface Api {
    @POST
    fun responseFollowUp(
            @Url responseUrl: String,
            @Body payload: String)
            : Observable<String>
}