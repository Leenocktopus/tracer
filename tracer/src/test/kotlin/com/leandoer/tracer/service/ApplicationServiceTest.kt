package com.leandoer.tracer.service

import com.leandoer.tracer.model.dto.application.GetApplicationDto
import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.repository.ApplicationRepository
import com.leandoer.tracer.repository.specification.ApplicationSpecification
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.util.*

@ExtendWith(MockKExtension::class)
class ApplicationServiceTest(
    @MockK private val applicationRepository: ApplicationRepository
) {

    private val applicationSpecification: ApplicationSpecification = spyk(ApplicationSpecification())
    private val applicationService = ApplicationService(applicationRepository, applicationSpecification)


    val applications = listOf(
        Application(1, "tracer-app"),
        Application(2, "emailer-app"),
    )

    val expectedApplications = listOf(
        GetApplicationDto(1, "tracer-app"),
        GetApplicationDto(2, "emailer-app"),
    )

    @Test
    fun `Test findAll`() {
        val pageable = PageRequest.of(0, 10)
        every { applicationRepository.findAll(pageable) } returns PageImpl(applications)

        val result = applicationService.findAll(pageable)

        assertThat(result, Matchers.containsInAnyOrder(*expectedApplications.toTypedArray()))

        verify(exactly = 1) { applicationRepository.findAll(pageable) }
    }

    @Test
    fun `Test findByName`() {
        val applicationName = "tracer-app"
        every { applicationRepository.findOne(any<Specification<Application>>()) } returns Optional.of(applications[0])

        val result = applicationService.findByName(applicationName)

        assertEquals(applications[0], result)

        verify(exactly = 1) { applicationSpecification.nameEquals(applicationName) }
        verify(exactly = 1) { applicationRepository.findOne(any<Specification<Application>>()) }
    }

    @Test
    fun `Test findByName when application is missing`() {
        val applicationName = "sender-app"
        every { applicationRepository.findOne(any<Specification<Application>>()) } returns Optional.empty()

        val result = applicationService.findByName(applicationName)

        assertNull(result)

        verify(exactly = 1) { applicationSpecification.nameEquals(applicationName) }
        verify(exactly = 1) { applicationRepository.findOne(any<Specification<Application>>()) }
    }

    @Test
    fun `Test create`() {
        val applicationName = "sender-app"
        val application = Application(null, applicationName)
        every { applicationRepository.save(application) } returns application.copy(id = 1)

        val result = applicationService.create(applicationName)

        assertEquals(application.copy(id = 1), result)

        verify(exactly = 1) { applicationRepository.save(application) }
    }
}