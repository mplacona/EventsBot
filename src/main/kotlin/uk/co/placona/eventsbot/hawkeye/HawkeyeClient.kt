package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.apache.log4j.Logger
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.placona.eventsbot.models.HawkeyeApiResponse
import java.time.LocalDateTime

class HawkeyeClient {
    companion object {
        private const val BASE_URL: String = "https://api.myjson.com"
        private val log = Logger.getLogger(HawkeyeClient::class.simpleName)

        fun getEventsAsync(country: String): Observable<HawkeyeApiResponse> {
            log.info("Hawkeye Client Called")
            log.info("query parameter $country")

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            val hawkeye = retrofit.create(Api::class.java)

            return hawkeye.getEvents(
                    LocalDateTime.now().toString(),
                    country,
                    System.getProperty("HAWKEYE_PASSWORD")
            ).subscribeOn(Schedulers.newThread())
        }
    }
}



