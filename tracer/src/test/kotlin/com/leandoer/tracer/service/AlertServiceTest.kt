package com.leandoer.tracer.service

import com.leandoer.tracer.exception.ApiException
import com.leandoer.tracer.exception.ErrorType.CONSTRAINT_VIOLATION_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.NOT_FOUND_EXCEPTION
import com.leandoer.tracer.model.dto.alert.AlertFilter
import com.leandoer.tracer.model.dto.alert.GetAlertDto
import com.leandoer.tracer.model.dto.alert.PostAlertDto
import com.leandoer.tracer.model.dto.alert.PutAlertDto
import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.Contact
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.repository.AlertRepository
import com.leandoer.tracer.repository.ContactRepository
import com.leandoer.tracer.repository.specification.AlertSpecification
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import liquibase.pro.packaged.it
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.util.*

@ExtendWith(MockKExtension::class)
class AlertServiceTest(
    @MockK private val alertRepository: AlertRepository,
    @MockK private val contactRepository: ContactRepository,
    @MockK private val labelService: LabelService
) {

    private val alertSpecification: AlertSpecification = spyk(AlertSpecification())
    private val alertService = AlertService(alertRepository, alertSpecification, contactRepository, labelService)

    val alerts = listOf(
        Alert(1, "google-alert", mutableSetOf(), mutableSetOf()),
        Alert(2, "important-alert", mutableSetOf(), mutableSetOf())
    )

    val expectedAlerts = listOf(
        GetAlertDto(1, "google-alert", listOf(), listOf()),
        GetAlertDto(2, "important-alert", listOf(), listOf())
    )

    @Test
    fun `Test findAll`() {
        val alertFilter = AlertFilter(null)
        val pageable = PageRequest.of(0, 10)
        every { alertRepository.findAll(any<Specification<Alert>>(), pageable) } returns PageImpl(alerts)

        val result = alertService.findAll(alertFilter, pageable)

        assertThat(result, containsInAnyOrder(*expectedAlerts.toTypedArray()))
        verify(exactly = 1) { alertSpecification.mappedToLabel(alertFilter.labelId) }
        verify(exactly = 1) { alertSpecification.fetchLabels() }
        verify(exactly = 1) { alertSpecification.fetchContacts() }
        verify(exactly = 1) { alertRepository.findAll(any<Specification<Alert>>(), pageable) }
    }

    @Test
    fun `Test findById`() {
        val alertId = 1
        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.of(alerts[0])

        val result = alertService.findById(alertId)

        assertEquals(expectedAlerts[0], result)
        verify(exactly = 1) { alertSpecification.idEquals(alertId) }
        verify(exactly = 1) { alertSpecification.fetchLabels() }
        verify(exactly = 1) { alertSpecification.fetchContacts() }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
    }

    @Test
    fun `Test findById when alert was not found`() {
        val alertId = 1
        val expectedMessage = "alert has not been found"
        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.empty()

        try {
            alertService.findById(alertId)
        } catch (e: ApiException) {
            assertEquals(e.error, NOT_FOUND_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            assertTrue(e.metadata.containsKey("alertId"))
            assertEquals(e.metadata["alertId"], alertId)
        }

        verify(exactly = 1) { alertSpecification.idEquals(alertId) }
        verify(exactly = 1) { alertSpecification.fetchLabels() }
        verify(exactly = 1) { alertSpecification.fetchContacts() }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
    }

    @Test
    fun `Test create`() {
        val alertDto = PostAlertDto(
            "alert",
            listOf(PostAlertDto.AlertContactDto("leandoer@gmail.com"), PostAlertDto.AlertContactDto("mail@ukr.net"))
        )

        val alert = Alert(
            null,
            alertDto.name,
            mutableSetOf()
        )

        alert.contacts.add(Contact(null, alertDto.contacts[0].email, alert))
        alert.contacts.add(Contact(null, alertDto.contacts[1].email, alert))

        val expected = GetAlertDto(
            1,
            alert.name,
            alert.contacts.mapIndexed { index, contact -> GetAlertDto.AlertContactDto(index + 1, contact.email) },
            listOf()
        )

        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.empty()
        every { alertRepository.save(alert) } returns alert.copy(id = 1)
        every { contactRepository.saveAll(alert.contacts) } returns alert.contacts.mapIndexed { index, contact ->
            contact.copy(
                id = index + 1
            )
        }

        val result = alertService.create(alertDto)

        assertEquals(result, expected)

        verify(exactly = 1) { alertSpecification.nameEquals(alertDto.name) }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
        verify(exactly = 1) { alertRepository.save(alert) }
        verify(exactly = 1) { contactRepository.saveAll(alert.contacts) }
    }

    @Test
    fun `Test create with duplicate alert name`() {
        val expectedMessage = "alert already exists"
        val alertDto = PostAlertDto(
            "alert",
            listOf(PostAlertDto.AlertContactDto("leandoer@gmail.com"), PostAlertDto.AlertContactDto("mail@ukr.net"))
        )

        val alert = Alert(
            1,
            alertDto.name,
            mutableSetOf()
        )

        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.of(alert)

        try {
            alertService.create(alertDto)
        } catch (e: ApiException) {
            assertEquals(e.error, CONSTRAINT_VIOLATION_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            assertTrue(e.metadata.containsKey("name"))
            assertEquals(e.metadata["name"], alertDto.name)
        }

        verify(exactly = 1) { alertSpecification.nameEquals(alertDto.name) }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
        verify(exactly = 0) { alertRepository.save(alert) }
        verify(exactly = 0) { contactRepository.saveAll(alert.contacts) }
    }

    @Test
    fun `Test update`() {
        val alertId = 4
        val alertDto = PutAlertDto(
            listOf(PutAlertDto.AlertContactDto("leandoer@gmail.com"), PutAlertDto.AlertContactDto("mail_a@ukr.net")),
            listOf(2, 3)
        )

        val alert = Alert(
            4,
            "alert",
            mutableSetOf(),
            mutableSetOf(),
            mutableSetOf(Label(1, "google"))
        )

        alert.contacts.add(Contact(1, alertDto.contacts[0].email, alert))
        alert.contacts.add(Contact(2, "mail@ukr.net", alert))

        val labels = listOf(Label(1, "google"), Label(2, "binary"), Label(3, "com"))

        val contactsToSave = listOf(
            Contact(null, alertDto.contacts[1].email, alert)
        )

        val contactsToDelete = alert.contacts.filter { it.email == "mail@ukr.net" }

        val expected = GetAlertDto(
            4,
            alert.name,
            alert.contacts.mapIndexed { index, contact -> GetAlertDto.AlertContactDto(index + 1, contact.email) },
            listOf(GetAlertDto.AlertLabelDto(2, "binary"), GetAlertDto.AlertLabelDto(3, "com"))
        )

        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.of(alert)
        every { contactRepository.saveAll(contactsToSave) } returns contactsToSave.mapIndexed { index, contact -> contact.copy(id = index + 4) }
        every { contactRepository.deleteAll(contactsToDelete) } returns Unit
        every { labelService.findByIdIn(alertDto.labels) } returns labels
        every { alertRepository.save(alert) } returns alert.copy(labels = labels.filterNot { it.id == 1}.toMutableSet())

        val result = alertService.update(alertId, alertDto)

        assertEquals(expected, result)

        verify(exactly = 1) { alertSpecification.idEquals(alertId) }
        verify(exactly = 1) { alertSpecification.fetchLabels() }
        verify(exactly = 1) { alertSpecification.fetchContacts() }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
        verify(exactly = 1) { contactRepository.saveAll(contactsToSave) }
        verify(exactly = 1) { contactRepository.deleteAll(contactsToDelete) }
        verify(exactly = 1) { alertRepository.save(alert) }
    }

    @Test
    fun `Test delete`() {
        val alertId = 1
        val alert = Alert(
            alertId,
            "alert",
            mutableSetOf()
        )

        alert.contacts.add(Contact(1, "leandoer@gmail.com", alert))
        alert.contacts.add(Contact(2, "mail@ukr.net", alert))

        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.of(alert)
        every { contactRepository.deleteAll(alert.contacts) } returns Unit
        every { alertRepository.deleteById(alertId) } returns Unit

        alertService.delete(alertId)

        verify(exactly = 1) { alertSpecification.idEquals(alertId) }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
        verify(exactly = 1) { contactRepository.deleteAll(alert.contacts) }
        verify(exactly = 1) { alertRepository.deleteById(alertId) }
    }

    @Test
    fun `Test delete alert that doesn't exist`() {
        val expectedMessage = "alert has not been found"
        val alertId = 2

        every { alertRepository.findOne(any<Specification<Alert>>()) } returns Optional.empty()

        try {
            alertService.delete(alertId)
        } catch (e: ApiException) {
            assertEquals(e.error, NOT_FOUND_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            assertTrue(e.metadata.containsKey("alertId"))
            assertEquals(e.metadata["alertId"], alertId)
        }

        verify(exactly = 1) { alertSpecification.idEquals(alertId) }
        verify(exactly = 1) { alertRepository.findOne(any<Specification<Alert>>()) }
        verify(exactly = 0) { contactRepository.deleteAll() }
        verify(exactly = 0) { alertRepository.deleteById(alertId) }

    }


}