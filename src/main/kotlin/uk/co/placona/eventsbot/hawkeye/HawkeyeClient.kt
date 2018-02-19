package uk.co.placona.eventsbot.hawkeye

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.placona.eventsbot.utilities.BasicAuthInterceptor

@Configuration
open class HawkeyeClient {
    private val baseUrl: String = "https://hawkeye.twilio.red"

    @Bean
    open fun okHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(BasicAuthInterceptor(System.getProperty("HAWKEYE_USERNAME"), System.getProperty("HAWKEYE_PASSWORD")))
        return httpClient.build()
    }

    @Bean
    open fun retrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build()
    }

    @Bean
    open fun hawkeyeService(retrofit: Retrofit): HawkeyeService {
        return retrofit.create(HawkeyeService::class.java)
    }
}



