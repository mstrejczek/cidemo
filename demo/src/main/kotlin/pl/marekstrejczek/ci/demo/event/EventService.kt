package pl.marekstrejczek.ci.demo.event

import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class EventService(val eventRepository: EventRepository) {
    val counter: AtomicLong

    fun saveEvent(event: Event) {
        eventRepository.saveEvent(event, counter.incrementAndGet())
    }

    init {
        this.counter = AtomicLong(0)
    }
}