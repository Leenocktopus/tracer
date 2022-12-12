package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.exception.ErrorType.VALIDATION_EXCEPTION
import com.leandoer.tracer.model.dto.test.GetTestDto
import com.leandoer.tracer.model.dto.test.TestFilter
import com.leandoer.tracer.model.entity.Package.MD
import com.leandoer.tracer.model.entity.Package.NIST
import com.leandoer.tracer.model.entity.TestType.FREQUENCY
import com.leandoer.tracer.model.entity.TestType.ONE
import com.leandoer.tracer.model.entity.TestType.SPECTRAL
import com.leandoer.tracer.model.entity.Test_
import com.leandoer.tracer.service.TestService
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
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@WebMvcTest(TestController::class)
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var testService: TestService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize, Sort.by(Test_.ID))
        val testFilter = TestFilter(false)
        val tests = listOf(
            GetTestDto(1, FREQUENCY.title, FREQUENCY, NIST),
            GetTestDto(2, SPECTRAL.title, SPECTRAL, NIST),
            GetTestDto(3, ONE.title, ONE, MD)
             )
        every {
            testService.findAll(testFilter, pageable)
        } returns PageImpl(tests, pageable, tests.size.toLong())

        mockMvc.get("/tests?parametrized=${testFilter.parametrized}")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(tests.size) }
                jsonPath("$.totalPages") { value(tests.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(tests[0].id) }
                jsonPath("$.content[0].title") { value(tests[0].title) }
                jsonPath("$.content[0].name") { value(tests[0].name.name) }
                jsonPath("$.content[0].type") { value(tests[0].type.name) }
                jsonPath("$.content[1].id") { value(tests[1].id) }
                jsonPath("$.content[1].title") { value(tests[1].title) }
                jsonPath("$.content[1].name") { value(tests[1].name.name) }
                jsonPath("$.content[1].type") { value(tests[1].type.name) }
                jsonPath("$.content[2].id") { value(tests[2].id) }
                jsonPath("$.content[2].title") { value(tests[2].title) }
                jsonPath("$.content[2].name") { value(tests[2].name.name) }
                jsonPath("$.content[2].type") { value(tests[2].type.name) }
            }
        verify { testService.findAll(testFilter, pageable) }
    }

    @Test
    fun `Test findAll with wrong request parameter type`() {

        mockMvc.get("/tests?parametrized=3")
            .andExpect {
                status { BAD_REQUEST }
                jsonPath("$.error") { value(VALIDATION_EXCEPTION.name) }
                jsonPath("$.code") { value(VALIDATION_EXCEPTION.code) }
                jsonPath("$.message") { value("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Boolean'; nested exception is java.lang.IllegalArgumentException: Invalid boolean value [3]") }
                jsonPath("$.metadata.field") { value("parametrized") }
                jsonPath("$.metadata.value") { value(3) }
            }
    }
    
}