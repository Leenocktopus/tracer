package com.leandoer.tracer.service

import com.leandoer.tracer.model.dto.test.GetTestDto
import com.leandoer.tracer.model.dto.test.TestFilter
import com.leandoer.tracer.model.entity.Package.MD
import com.leandoer.tracer.model.entity.Package.NIST
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.TestType.ONE
import com.leandoer.tracer.model.entity.TestType.SPECTRAL
import com.leandoer.tracer.repository.TestRepository
import com.leandoer.tracer.repository.specification.TestSpecification
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import com.leandoer.tracer.model.entity.Test as TestEntity

@ExtendWith(MockKExtension::class)
class TestServiceTest(
    @MockK private val testRepository: TestRepository,
) {

    private val testSpecification: TestSpecification = spyk(TestSpecification())
    private val testService = TestService(testRepository, testSpecification)

    val tests = listOf(
        TestEntity(1, FREQUENCY, NIST, false),
        TestEntity(2, SPECTRAL, NIST, false),
        TestEntity(16, ONE, MD, false)
    )

    val expectedTests = listOf(
        GetTestDto(1, FREQUENCY.title, FREQUENCY, NIST),
        GetTestDto(2, SPECTRAL.title, SPECTRAL, NIST),
        GetTestDto(16, ONE.title, ONE, MD)
    )

    @Test
    fun `Test findAll`() {
        val testFilter = TestFilter(true)
        val pageable = PageRequest.of(0, 10)
        every { testRepository.findAll(any<Specification<TestEntity>>(), pageable) } returns PageImpl(tests)

        val result = testService.findAll(testFilter, pageable)

        MatcherAssert.assertThat(result, Matchers.containsInAnyOrder(*expectedTests.toTypedArray()))

        verify(exactly = 1) { testSpecification.isParametrized(testFilter.parametrized) }
        verify(exactly = 1) { testRepository.findAll(any<Specification<TestEntity>>(), pageable) }
    }

}