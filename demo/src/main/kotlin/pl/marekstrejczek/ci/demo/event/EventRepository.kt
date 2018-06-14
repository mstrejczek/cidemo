package pl.marekstrejczek.ci.demo.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import mu.KLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Repository
class EventRepository(
        dataSource: DataSource,
        @Qualifier("saveEventQuery") val saveEventQuery: String,
        objectMapper: ObjectMapper) {

    companion object: KLogging()

    val jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)
    val objectWriter: ObjectWriter = objectMapper.writer()

    fun saveEvent(event: Event, sequence: Long) {
        logger.info("Repo called, sequence = $sequence")
        jdbcTemplate.update(saveEventQuery, event.time, event.type, sequence, objectWriter.writeValueAsString(event.fields))
    }
}