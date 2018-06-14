package pl.marekstrejczek.ci.demo.event

import java.time.LocalDateTime

data class Event(val time: LocalDateTime, val type: String, val fields: Map<String, String>)