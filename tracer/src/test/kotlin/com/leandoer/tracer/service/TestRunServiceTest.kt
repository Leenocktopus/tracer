package com.leandoer.tracer.service

import com.leandoer.tracer.model.dto.testrun.GetTestRunDto
import com.leandoer.tracer.model.dto.testrun.TestRunFilter
import com.leandoer.tracer.model.entity.Application
import com.leandoer.tracer.model.entity.Package.MD
import com.leandoer.tracer.model.entity.Package.NIST
import com.leandoer.tracer.model.entity.TestRun
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.TestType.ONE
import com.leandoer.tracer.model.entity.TestType.SPECTRAL
import com.leandoer.tracer.model.entity.Trace
import com.leandoer.tracer.repository.TestRunRepository
import com.leandoer.tracer.repository.specification.TestRunSpecification
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.LocalDateTime
import com.leandoer.tracer.model.entity.Test as TestEntity

@ExtendWith(MockKExtension::class)
class TestRunServiceTest(
    @MockK private val testRunRepository: TestRunRepository,
) {

    private val testRunSpecification: TestRunSpecification = spyk(TestRunSpecification())
    private val testRunService = TestRunService(testRunRepository, testRunSpecification)

    val application = Application(1, "tracer-app")

    val trace = Trace(
        1,
        "9394212441794",
        application,
        LocalDateTime.now()
    )

    val testRuns = listOf(
        TestRun(1, trace, TestEntity(1, FREQUENCY, NIST, false), BigDecimal.valueOf(0.18103), true),
        TestRun(2, trace, TestEntity(2, SPECTRAL, NIST, false), BigDecimal.valueOf(0.031), false),
        TestRun(3, trace, TestEntity(16, ONE, MD, false), BigDecimal.valueOf(0.18103), null)
    )

    val expectedTestRuns = listOf(
        GetTestRunDto(1, FREQUENCY.title, BigDecimal.valueOf(0.18103), true),
        GetTestRunDto(2, SPECTRAL.title, BigDecimal.valueOf(0.031), false),
        GetTestRunDto(3, ONE.title, BigDecimal.valueOf(0.18103), null)
    )

    @Test
    fun `Test findAll`() {
        val testRunFilter = TestRunFilter(trace.id)
        val pageable = PageRequest.of(0, 10)
        every { testRunRepository.findAll(any<Specification<TestRun>>(), pageable) } returns PageImpl(testRuns)

        val result = testRunService.findAll(testRunFilter, pageable)

        assertThat(result, Matchers.containsInAnyOrder(*expectedTestRuns.toTypedArray()))

        verify(exactly = 1) { testRunSpecification.traceEquals(testRunFilter.traceId) }
        verify(exactly = 1) { testRunSpecification.fetchTests() }
        verify(exactly = 1) { testRunRepository.findAll(any<Specification<TestRun>>(), pageable) }
    }

}