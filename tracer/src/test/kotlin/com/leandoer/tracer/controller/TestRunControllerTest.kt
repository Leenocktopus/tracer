package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.model.dto.testrun.GetTestRunDto
import com.leandoer.tracer.model.dto.testrun.TestRunFilter
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.TestType.BLOCK_FREQUENCY
import com.leandoer.tracer.model.entity.TestType.ONE
import com.leandoer.tracer.service.TestRunService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus.OK
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.math.BigDecimal


@WebMvcTest(TestRunController::class)
@AutoConfigureMockMvc
class TestRunControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var testRunService: TestRunService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize)
        val testRunFilter = TestRunFilter(1)
        val testRuns = listOf(
            GetTestRunDto(1, FREQUENCY.title, BigDecimal.valueOf(0.1), true),
            GetTestRunDto(2, BLOCK_FREQUENCY.title, BigDecimal.valueOf(0.391), false),
            GetTestRunDto(3, ONE.title, BigDecimal.valueOf(0.99999), null)
             )
        every {
            testRunService.findAll(testRunFilter, pageable)
        } returns PageImpl(testRuns, pageable, testRuns.size.toLong())

        mockMvc.get("/test-runs?traceId=${testRunFilter.traceId}")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(testRuns.size) }
                jsonPath("$.totalPages") { value(testRuns.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(testRuns[0].id) }
                jsonPath("$.content[0].test") { value(testRuns[0].test) }
                jsonPath("$.content[0].testResult") { value(testRuns[0].testResult) }
                jsonPath("$.content[0].random") { value(testRuns[0].random) }
                jsonPath("$.content[1].id") { value(testRuns[1].id) }
                jsonPath("$.content[1].test") { value(testRuns[1].test) }
                jsonPath("$.content[1].testResult") { value(testRuns[1].testResult) }
                jsonPath("$.content[1].random") { value(testRuns[1].random) }
                jsonPath("$.content[2].id") { value(testRuns[2].id) }
                jsonPath("$.content[2].test") { value(testRuns[2].test) }
                jsonPath("$.content[2].testResult") { value(testRuns[2].testResult) }
                jsonPath("$.content[2].random") { value(testRuns[2].random) }
            }
        verify { testRunService.findAll(testRunFilter, pageable) }
    }
    
}