package uk.co.placona.eventsbot

import io.reactivex.schedulers.Schedulers
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import uk.co.placona.eventsbot.hawkeye.HawkeyeRepository


@SpringBootApplication
@EnableCaching
open class EventsBotApp  : CommandLineRunner {
    @Autowired
    lateinit var hawkeyeRepository: HawkeyeRepository

    private val log = Logger.getLogger(EventsBotApp::class.simpleName)

    companion object {
        lateinit var tags: Map<String, Int>
        lateinit var countries: Map<String, Int>
    }

    override fun run(vararg args: String?) {

        // Load tags in memory
        hawkeyeRepository.getTags()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { t ->
                            tags = t
                        },
                        { error -> log.error(error) }
                )

        // Load countries in memory
        hawkeyeRepository.getCountries()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { t ->
                            countries = t
                        },
                        { error -> log.error(error) }
                )
    }
}

fun main(args: Array<String>) {
    System.setProperty("HAWKEYE_USERNAME", System.getenv("HAWKEYE_USERNAME"))
    System.setProperty("HAWKEYE_PASSWORD", System.getenv("HAWKEYE_PASSWORD"))
    System.setProperty("VERIFICATION_TOKEN", System.getenv("VERIFICATION_TOKEN"))
    SpringApplication.run(EventsBotApp::class.java, *args)
}