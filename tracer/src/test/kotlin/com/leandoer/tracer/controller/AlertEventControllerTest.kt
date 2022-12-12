package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.configuration.DATE_TIME_FORMAT
import com.leandoer.tracer.model.dto.alertevent.AlertEventFilter
import com.leandoer.tracer.model.dto.alertevent.GetAlertEventDto
import com.leandoer.tracer.model.dto.alertevent.PutEventAlertDto
import com.leandoer.tracer.model.entity.AlertEvent_
import com.leandoer.tracer.model.entity.AlertEvent_.TRACE
import com.leandoer.tracer.model.entity.AlertStatus.NEW
import com.leandoer.tracer.model.entity.AlertStatus.RESOLVED
import com.leandoer.tracer.model.entity.Trace_
import com.leandoer.tracer.model.entity.Trace_.GENERATED_AT
import com.leandoer.tracer.service.AlertEventService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@WebMvcTest(AlertEventController::class)
@AutoConfigureMockMvc
class AlertEventControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var alertEventService: AlertEventService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize, Sort.by(DESC, "${TRACE}.${GENERATED_AT}"))
        val alertEventFilter = AlertEventFilter(1)
        val alertEvents = listOf(
            GetAlertEventDto(1, "foo bar", 101, RESOLVED, LocalDateTime.now().minusDays(1)),
            GetAlertEventDto(2, "bar baz", 102, NEW, LocalDateTime.now()),
        )

        every {
            alertEventService.findAll(alertEventFilter, pageable)
        } returns PageImpl(alertEvents)

        mockMvc.get("/alert-events?alertId=${alertEventFilter.alertId}")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(alertEvents.size) }
                jsonPath("$.totalPages") { value(alertEvents.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(alertEvents[0].id) }
                jsonPath("$.content[0].reason") { value(alertEvents[0].reason) }
                jsonPath("$.content[0].traceId") { value(alertEvents[0].traceId) }
                jsonPath("$.content[0].status") { value(alertEvents[0].status.name) }
                jsonPath("$.content[0].generatedAt") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(
                            alertEvents[0].generatedAt
                        )
                    )
                }
                jsonPath("$.content[1].id") { value(alertEvents[1].id) }
                jsonPath("$.content[1].reason") { value(alertEvents[1].reason) }
                jsonPath("$.content[1].traceId") { value(alertEvents[1].traceId) }
                jsonPath("$.content[1].status") { value(alertEvents[1].status.name) }
                jsonPath("$.content[1].generatedAt") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(
                            alertEvents[1].generatedAt
                        )
                    )
                }
            }

        verify { alertEventService.findAll(alertEventFilter, pageable) }
    }

    @Test
    fun `Test update`() {
        val alertEventId = 1
        val alertEvent = PutEventAlertDto(RESOLVED)
        val updatedAlertEvent = GetAlertEventDto(1, "foo bar", 101, RESOLVED, LocalDateTime.now().minusDays(1))


        every {
            alertEventService.update(alertEventId, alertEvent)
        } returns updatedAlertEvent

        mockMvc.put("/alert-events/${alertEventId}") {
            content = mapper.writeValueAsString(alertEvent)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { OK }
                jsonPath("$.id") { value(updatedAlertEvent.id) }
                jsonPath("$.reason") { value(updatedAlertEvent.reason) }
                jsonPath("$.traceId") { value(updatedAlertEvent.traceId) }
                jsonPath("$.status") { value(updatedAlertEvent.status.name) }
                jsonPath("$.generatedAt") {
                    value(
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(
                            updatedAlertEvent.generatedAt
                        )
                    )
                }
            }

        verify { alertEventService.update(alertEventId, alertEvent) }
    }

}