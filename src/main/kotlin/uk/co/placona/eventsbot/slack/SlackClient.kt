package uk.co.placona.eventsbot.slack

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.log4j.Logger
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import uk.co.placona.eventsbot.models.Attachments


class SlackClient {
    companion object {
        private const val BASE_URL: String = "https://hooks.slack.com"
        private val log = Logger.getLogger(SlackClient::class.simpleName)

        fun responseFollowUp(responseUrl: String, payload: Attachments): Observable<String> {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build()
            val slack = retrofit.create(Api::class.java)
            val jsonStr = Gson().toJson(payload)

            return slack
                    .responseFollowUp(responseUrl, jsonStr)
                    .subscribeOn(Schedulers.newThread())
        }
    }
}