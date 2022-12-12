package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.model.dto.application.GetApplicationDto
import com.leandoer.tracer.model.entity.Application_
import com.leandoer.tracer.service.ApplicationService
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
import org.springframework.http.HttpStatus.OK
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@WebMvcTest(ApplicationController::class)
@AutoConfigureMockMvc
class ApplicationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var applicationService: ApplicationService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize, Sort.by(Application_.ID))
        val applications = listOf(
            GetApplicationDto(1, "sender"),
            GetApplicationDto(1, "emailer"),
        )
        every {
            applicationService.findAll(pageable)
        } returns PageImpl(applications, pageable, applications.size.toLong())

        mockMvc.get("/applications")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(applications.size) }
                jsonPath("$.totalPages") { value(applications.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(applications[0].id) }
                jsonPath("$.content[0].name") { value(applications[0].name) }
                jsonPath("$.content[0].id") { value(applications[0].id) }
                jsonPath("$.content[0].name") { value(applications[0].name) }
            }
        verify { applicationService.findAll(pageable) }
    }

}