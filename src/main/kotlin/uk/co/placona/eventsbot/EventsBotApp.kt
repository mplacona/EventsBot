package uk.co.placona.eventsbot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class EventsBotApp

fun main(args: Array<String>) {
    System.setProperty("HAWKEYE_USERNAME", System.getenv("HAWKEYE_USERNAME"))
    System.setProperty("HAWKEYE_PASSWORD", System.getenv("HAWKEYE_PASSWORD"))
    SpringApplication.run(EventsBotApp::class.java, *args)
}