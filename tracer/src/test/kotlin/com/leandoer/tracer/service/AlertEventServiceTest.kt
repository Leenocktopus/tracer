package com.leandoer.tracer.service

import com.leandoer.tracer.exception.ApiException
import com.leandoer.tracer.exception.ErrorType.NOT_FOUND_EXCEPTION
import com.leandoer.tracer.model.dto.alertevent.AlertEventFilter
import com.leandoer.tracer.model.dto.alertevent.GetAlertEventDto
import com.leandoer.tracer.model.dto.alertevent.PutEventAlertDto
import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.AlertEvent
import com.leandoer.tracer.model.entity.AlertStatus.NEW
import com.leandoer.tracer.model.entity.AlertStatus.RESOLVED
import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.repository.AlertEventRepository
import com.leandoer.tracer.repository.specification.AlertEventSpecification
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification

import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class AlertEventServiceTest(
    @MockK private val alertEventRepository: AlertEventRepository
) {

    private val alertEventSpecification: AlertEventSpecification = spyk(AlertEventSpecification())
    private val alertEventService = AlertEventService(alertEventRepository, alertEventSpecification)

    val application = Application(1, "tracer-app")

    val alert = Alert(9, "google-alert", mutableSetOf(), mutableSetOf())
    val firstTrace = Trace(
        101,
        "9394212441794",
        application,
        LocalDateTime.now()
    )

    val secondTrace = Trace(
        102,
        "238057230492",
        application,
        LocalDateTime.now()
    )

    val alertEvents = listOf(
        AlertEvent(1, alert, "foo bar", RESOLVED, firstTrace),
        AlertEvent(2, alert, "bar baz", NEW, secondTrace)
    )

    val expectedAlertEvents = listOf(
        GetAlertEventDto(1, "foo bar", 101, RESOLVED, firstTrace.generatedAt),
        GetAlertEventDto(2, "bar baz", 102, NEW, secondTrace.generatedAt),
    )

    @Test
    fun `Test findAll`() {
        val alertEventFilter = AlertEventFilter(9)
        val pageable = PageRequest.of(0, 10)
        every { alertEventRepository.findAll(any<Specification<AlertEvent>>(), pageable) } returns PageImpl(alertEvents)

        val result = alertEventService.findAll(alertEventFilter, pageable)

        assertThat(result, containsInAnyOrder(*expectedAlertEvents.toTypedArray()))
        verify(exactly = 1) { alertEventSpecification.alertIdEquals(alertEventFilter.alertId) }
        verify(exactly = 1) { alertEventSpecification.fetchTrace() }
        verify(exactly = 1) { alertEventRepository.findAll(any<Specification<AlertEvent>>(), pageable) }
    }

    @Test
    fun `Test findByIdInternal`() {
        val alertEventId = 1
        val alertEventFromDB = AlertEvent(1, alert, "foo bar", NEW, firstTrace)

        every { alertEventRepository.findOne(any<Specification<AlertEvent>>()) } returns Optional.of(alertEventFromDB)

        val result = alertEventService.findByIdInternal(alertEventId)

        assertEquals(result, alertEventFromDB)

        verify(exactly = 1) { alertEventSpecification.idEquals(alertEventId) }
        verify(exactly = 1) { alertEventSpecification.fetchTrace() }
        verify(exactly = 1) { alertEventRepository.findOne(any<Specification<AlertEvent>>()) }
    }

    @Test
    fun `Test findByIdInternal when alert event was not found`() {
        val expectedMessage = "alert event has not been found"
        val alertEventId = 1
        every { alertEventRepository.findOne(any<Specification<AlertEvent>>()) } returns Optional.empty()

        try {
            alertEventService.findByIdInternal(alertEventId)
        } catch (e: ApiException) {
            assertEquals(e.error, NOT_FOUND_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            Assertions.assertTrue(e.metadata.containsKey("alertEventId"))
            assertEquals(e.metadata["alertEventId"], alertEventId)
        }

        verify(exactly = 1) { alertEventSpecification.idEquals(alertEventId) }
        verify(exactly = 1) { alertEventSpecification.fetchTrace() }
        verify(exactly = 1) { alertEventRepository.findOne(any<Specification<AlertEvent>>()) }
    }

    @Test
    fun `Test update`() {
        val alertEventId = 1
        val alertEvent = PutEventAlertDto(RESOLVED)

        val alertEventFromDB = AlertEvent(1, alert, "foo bar", NEW, firstTrace)
        val alertEventToSave = alertEventFromDB.copy(status = alertEvent.status)

        val expectedAlert = GetAlertEventDto(1, "foo bar", 101, RESOLVED, firstTrace.generatedAt)

        every { alertEventRepository.findOne(any<Specification<AlertEvent>>()) } returns Optional.of(alertEventFromDB)
        every { alertEventRepository.save(alertEventToSave) } returns alertEventToSave

        val result = alertEventService.update(alertEventId, alertEvent)

        assertEquals(result, expectedAlert)

        verify(exactly = 1) { alertEventSpecification.idEquals(alertEventId) }
        verify(exactly = 1) { alertEventRepository.findOne(any<Specification<AlertEvent>>()) }
        verify(exactly = 1) { alertEventRepository.save(alertEventToSave) }
    }

}