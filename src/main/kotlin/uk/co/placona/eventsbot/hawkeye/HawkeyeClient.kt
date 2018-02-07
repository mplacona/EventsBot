package uk.co.placona.eventsbot.hawkeye

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.log4j.Logger
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.placona.eventsbot.models.HawkeyeApiResponse
import uk.co.placona.eventsbot.utilities.BasicAuthInterceptor
import uk.co.placona.eventsbot.utilities.countrySpell
import java.time.LocalDateTime

class HawkeyeClient {
    companion object {
        private const val BASE_URL: String = "https://hawkeye.twilio.red"
        private val log = Logger.getLogger(HawkeyeClient::class.simpleName)

        fun getEventsAsync(country: String): Observable<HawkeyeApiResponse> {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            httpClient.addInterceptor(BasicAuthInterceptor(System.getProperty("HAWKEYE_USERNAME"), System.getProperty("HAWKEYE_PASSWORD")))

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build()
            val hawkeye = retrofit.create(Api::class.java)

            return hawkeye.getEvents(
                    LocalDateTime.now().toString(),
                    countrySpell(country),
                    System.getProperty("HAWKEYE_PASSWORD")
            ).subscribeOn(Schedulers.newThread())
        }
    }
}



