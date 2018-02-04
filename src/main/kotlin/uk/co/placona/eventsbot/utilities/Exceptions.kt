package uk.co.placona.eventsbot.utilities

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Unauthorised")
class UnauthorisedException: RuntimeException()