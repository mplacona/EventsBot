package uk.co.placona.eventsbot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
open class EventsBotApp

fun main(args: Array<String>) {
    System.setProperty("HAWKEYE_USERNAME", System.getenv("HAWKEYE_USERNAME"))
    System.setProperty("HAWKEYE_PASSWORD", System.getenv("HAWKEYE_PASSWORD"))
    System.setProperty("VERIFICATION_TOKEN", System.getenv("VERIFICATION_TOKEN"))
    SpringApplication.run(EventsBotApp::class.java, *args)
}