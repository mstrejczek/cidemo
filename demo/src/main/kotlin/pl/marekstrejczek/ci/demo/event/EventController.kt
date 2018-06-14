package pl.marekstrejczek.ci.demo.event

import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(val eventService: EventService) {
    companion object: KLogging()

    @PostMapping("/events")
    fun saveEvent(@RequestBody event: Event) {
        logger.info("Received saveEvent request: $event")
        eventService.saveEvent(event)
    }

}