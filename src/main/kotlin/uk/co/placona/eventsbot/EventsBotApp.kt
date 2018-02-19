package uk.co.placona.eventsbot

import io.reactivex.schedulers.Schedulers
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import uk.co.placona.eventsbot.hawkeye.HawkeyeRepository
import uk.co.placona.eventsbot.hawkeye.HawkeyeService
import javax.annotation.PostConstruct


@SpringBootApplication
@EnableCaching
open class EventsBotApp {

    @Autowired
    private
    lateinit var hawkeyeService: HawkeyeService

    @Autowired
    private
    lateinit var hawkeyeRepository: HawkeyeRepository

    private val log = Logger.getLogger(EventsBotApp::class.simpleName)

    @PostConstruct
    fun init() {
        hawkeyeRepository = HawkeyeRepository(hawkeyeService)

        hawkeyeRepository.getTags()
                .subscribeOn(Schedulers.io())
                .subscribe()

        hawkeyeRepository.getCountries()
                .subscribeOn(Schedulers.io())
                .subscribe()

        log.info("Cache initialised")
    }
}

fun main(args: Array<String>) {
    System.setProperty("HAWKEYE_USERNAME", System.getenv("HAWKEYE_USERNAME"))
    System.setProperty("HAWKEYE_PASSWORD", System.getenv("HAWKEYE_PASSWORD"))
    System.setProperty("VERIFICATION_TOKEN", System.getenv("VERIFICATION_TOKEN"))
    SpringApplication.run(EventsBotApp::class.java, *args)
}