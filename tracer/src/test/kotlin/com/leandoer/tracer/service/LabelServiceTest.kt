package com.leandoer.tracer.service

import com.leandoer.tracer.exception.ApiException
import com.leandoer.tracer.exception.ErrorType.CONSTRAINT_VIOLATION_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.NOT_FOUND_EXCEPTION
import com.leandoer.tracer.model.dto.label.GetLabelDto
import com.leandoer.tracer.model.dto.label.PostLabelDto
import com.leandoer.tracer.model.dto.label.PutLabelDto
import com.leandoer.tracer.model.entity.Alert
import com.leandoer.tracer.model.entity.Label
import com.leandoer.tracer.model.entity.Package.MD
import com.leandoer.tracer.model.entity.Package.NIST
import com.leandoer.tracer.model.entity.TestType.BLOCK_FREQUENCY
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.TestType.ONE
import com.leandoer.tracer.model.entity.TestType.SPECTRAL
import com.leandoer.tracer.repository.LabelRepository
import com.leandoer.tracer.repository.TestRepository
import com.leandoer.tracer.repository.specification.LabelSpecification
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import java.util.*
import com.leandoer.tracer.model.entity.Test as TestEntity

@ExtendWith(MockKExtension::class)
class LabelServiceTest(
    @MockK private val labelRepository: LabelRepository,
    @MockK private val testRepository: TestRepository
) {

    private val labelSpecification: LabelSpecification = spyk(LabelSpecification())
    private val labelService = LabelService(labelRepository, labelSpecification, testRepository)


    val labels = listOf(
        Label(
            1, "google", mutableSetOf(
                TestEntity(1, FREQUENCY, NIST, false),
                TestEntity(2, SPECTRAL, NIST, false),
                TestEntity(16, ONE, MD, false)
            )
        ),
        Label(2, "binary")
    )

    val expectedLabels = listOf(
        GetLabelDto(
            1, "google", listOf(
                GetLabelDto.LabelTestDto(1, FREQUENCY.title, FREQUENCY, NIST),
                GetLabelDto.LabelTestDto(2, SPECTRAL.title, SPECTRAL, NIST),
                GetLabelDto.LabelTestDto(16, ONE.title, ONE, MD)
            )
        ),
        GetLabelDto(2, "binary")
    )

    @Test
    fun `Test findAll`() {
        val pageable = PageRequest.of(0, 10)
        every { labelRepository.findAll(any<Specification<Label>>(), pageable) } returns PageImpl(labels)

        val result = labelService.findAll(pageable)

        assertThat(result, Matchers.containsInAnyOrder(*expectedLabels.toTypedArray()))
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelRepository.findAll(any<Specification<Label>>(), pageable) }
    }

    @Test
    fun `Test findById`() {
        val labelId = 1
        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(labels[0])

        val result = labelService.findById(labelId)

        assertEquals(expectedLabels[0], result)

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
    }

    @Test
    fun `Test findById when label was not found`() {
        val expectedMessage = "label has not been found"
        val labelId = 2
        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.empty()

        try {
            labelService.findById(labelId)
        } catch (e: ApiException) {
            assertEquals(e.error, NOT_FOUND_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            assertTrue(e.metadata.containsKey("labelId"))
            assertEquals(e.metadata["labelId"], labelId)
        }

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
    }

    @Test
    fun `Test findByLabel`() {
        val label = "google"
        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(labels[0])

        val result = labelService.findByLabel(label)

        assertEquals(labels[0], result)

        verify(exactly = 1) { labelSpecification.labelEquals(label) }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
    }

    @Test
    fun `Test findByLabel when label was not found`() {
        val expectedMessage = "label has not been found"
        val label = "google"
        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.empty()

        try {
            labelService.findByLabel(label)
        } catch (e: ApiException) {
            assertEquals(e.error, NOT_FOUND_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            assertTrue(e.metadata.containsKey("label"))
            assertEquals(e.metadata["label"], label)
        }

        verify(exactly = 1) { labelSpecification.labelEquals(label) }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
    }


    @Test
    fun `Test findByIdIn`() {
        val ids = listOf(1, 2)
        every { labelRepository.findAll(any<Specification<Label>>()) } returns labels

        val result = labelService.findByIdIn(ids)

        assertThat(result, Matchers.containsInAnyOrder(*labels.toTypedArray()))

        verify(exactly = 1) { labelSpecification.idIn(ids) }
        verify(exactly = 1) { labelRepository.findAll(any<Specification<Label>>()) }
    }

    @Test
    fun `Test findByIdIn when label was not found`() {
        val expectedMessage = "label has not been found"
        val ids = listOf(1, 2, 3)
        every { labelRepository.findAll(any<Specification<Label>>()) } returns labels

        try {
            labelService.findByIdIn(ids)
        } catch (e: ApiException) {
            assertEquals(e.error, NOT_FOUND_EXCEPTION)
            assertEquals(e.message, expectedMessage)
            assertTrue(e.metadata.containsKey("labelId"))
            assertEquals(e.metadata["labelId"], ids.last())
        }

        verify(exactly = 1) { labelSpecification.idIn(ids) }
        verify(exactly = 1) { labelRepository.findAll(any<Specification<Label>>()) }
    }

    @Test
    fun `Test create`() {
        val labelDto = PostLabelDto("hardware")

        val labelToSave = Label(null, labelDto.label)

        val expected = GetLabelDto(1, labelDto.label)

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.empty()
        every { labelRepository.save(labelToSave) } returns labelToSave.copy(id = 1)

        val result = labelService.create(labelDto)

        assertEquals(expected, result)

        verify(exactly = 1) { labelSpecification.labelEquals(labelDto.label) }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
        verify(exactly = 1) { labelRepository.save(labelToSave) }
    }

    @Test
    fun `Test create duplicate label`() {
        val expectedMessage = "label already exists"
        val labelDto = PostLabelDto("hardware")

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(Label(1, "hardware"))

        try {
            labelService.create(labelDto)
        } catch (e: ApiException) {
            assertEquals(CONSTRAINT_VIOLATION_EXCEPTION, e.error)
            assertEquals(expectedMessage, e.message)
            assertTrue(e.metadata.containsKey("label"))
            assertEquals(labelDto.label, e.metadata["label"])
        }

        verify(exactly = 1) { labelSpecification.labelEquals(labelDto.label) }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
    }

    @Test
    fun `Test update`() {
        val labelId = 3
        val labelDto = PutLabelDto(listOf(1))
        val tests = listOf(
            TestEntity(1, FREQUENCY, NIST, false),
            TestEntity(2, BLOCK_FREQUENCY, NIST, true),
            TestEntity(16, ONE, MD, false)
        )

        val labelFromDB = Label(1, "hardware")

        val labelToSave = labelFromDB.copy(tests = tests.filter { it.id in labelDto.tests }.toMutableSet())

        val expected = GetLabelDto(
            1,
            "hardware",
            listOf(GetLabelDto.LabelTestDto(tests[0].id!!, tests[0].testType.title, tests[0].testType, tests[0].type))
        )

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(labelFromDB)
        every { testRepository.findAll() } returns tests
        every { labelRepository.save(labelToSave) } returns labelToSave

        val result = labelService.update(labelId, labelDto)

        assertEquals(expected, result)

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
        verify(exactly = 1) { testRepository.findAll() }
        verify(exactly = 1) { labelRepository.save(labelToSave) }
    }

    @Test
    fun `Test update with parametrized test`() {
        val expectedMessage = "parametrized tests can't be added to labels"
        val labelId = 3
        val labelDto = PutLabelDto(listOf(2))
        val tests = listOf(
            TestEntity(1, FREQUENCY, NIST, false),
            TestEntity(2, BLOCK_FREQUENCY, NIST, true),
            TestEntity(16, ONE, MD, false)
        )

        val labelFromDB = Label(1, "hardware")

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(labelFromDB)
        every { testRepository.findAll() } returns tests

        try {
            labelService.update(labelId, labelDto)
        } catch (e: ApiException) {
            assertEquals(CONSTRAINT_VIOLATION_EXCEPTION, e.error)
            assertEquals(expectedMessage, e.message)
            assertTrue(e.metadata.containsKey("testIds"))
            assertEquals(labelDto.tests, e.metadata["testIds"])
        }

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelRepository.findOne(any<Specification<Label>>()) }
        verify(exactly = 1) { testRepository.findAll() }
        verify(exactly = 0) { testRepository.save(any()) }
    }

    @Test
    fun `Test delete`() {
        val labelId = 3

        val label = Label(3, "google")

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(label)
        every { labelRepository.deleteById(labelId) } returns Unit

        labelService.delete(labelId)

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 1) { labelRepository.deleteById(labelId) }
    }

    @Test
    fun `Test delete label with mapped alerts`() {
        val expectedMessage = "can't delete label with mapped alerts"
        val labelId = 3

        val label = Label(3, "google", mutableSetOf(), mutableSetOf(Alert(1, "google-alert")))

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.of(label)
        every { labelRepository.deleteById(labelId) } returns Unit

        try {
            labelService.delete(labelId)
        } catch (e: ApiException) {
            assertEquals(CONSTRAINT_VIOLATION_EXCEPTION, e.error)
            assertEquals(expectedMessage, e.message)
            assertTrue(e.metadata.containsKey("label"))
            assertEquals(label.label, e.metadata["label"])
        }

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 0) { labelRepository.deleteById(labelId) }
    }

    @Test
    fun `Test delete when label doesn't exist`() {
        val expectedMessage = "label has not been found"
        val labelId = 3

        every { labelRepository.findOne(any<Specification<Label>>()) } returns Optional.empty()

        try {
            labelService.delete(labelId)
        } catch (e: ApiException) {
            assertEquals(NOT_FOUND_EXCEPTION, e.error)
            assertEquals(expectedMessage, e.message)
            assertTrue(e.metadata.containsKey("labelId"))
            assertEquals(labelId, e.metadata["labelId"])
        }

        verify(exactly = 1) { labelSpecification.idEquals(labelId) }
        verify(exactly = 1) { labelSpecification.fetchTests() }
        verify(exactly = 1) { labelSpecification.fetchAlerts() }
        verify(exactly = 0) { labelRepository.deleteById(labelId) }
    }

}