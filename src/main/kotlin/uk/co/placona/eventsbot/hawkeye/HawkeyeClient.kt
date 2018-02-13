package uk.co.placona.eventsbot.hawkeye

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.placona.eventsbot.utilities.BasicAuthInterceptor

class HawkeyeClient {
    companion object {
        private const val BASE_URL: String = "https://hawkeye.twilio.red"

        fun create(): Api{
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
            return retrofit.create(Api::class.java)
        }
    }
}



