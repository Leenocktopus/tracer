package com.leandoer.tracer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leandoer.tracer.configuration.DATE_TIME_FORMAT
import com.leandoer.tracer.model.dto.trace.GetTraceDto
import com.leandoer.tracer.model.dto.trace.TraceFilter
import com.leandoer.tracer.model.entity.Trace_
import com.leandoer.tracer.service.TraceService
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@WebMvcTest(TraceController::class)
@AutoConfigureMockMvc
class TraceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var traceService: TraceService

    @Autowired
    private lateinit var mapper: ObjectMapper


    @Test
    fun `Test findAll`() {
        val pageSize = 10
        val pageable = PageRequest.of(0, pageSize, Sort.by(DESC, Trace_.GENERATED_AT))
        val traceFilter = TraceFilter(listOf(), listOf(), null, null, null)
        val traces = listOf(
            GetTraceDto(
                1,
                "421481301331",
                "sender",
                LocalDateTime.of(2022, 10, 10, 10, 30),
                listOf(GetTraceDto.TraceLabelDto(1, "google"), GetTraceDto.TraceLabelDto(2, "decimal"))
            ),
            GetTraceDto(
                1,
                "000001010101010010100101001011",
                "emailer",
                LocalDateTime.of(2022, 10, 19, 17, 3),
                listOf(GetTraceDto.TraceLabelDto(3, "binary"))
            )
        )
        every {
            traceService.findAll(traceFilter, pageable)
        } returns PageImpl(traces, pageable, traces.size.toLong())

        mockMvc.get("/traces")
            .andExpect {
                status { OK }
                jsonPath("$.totalElements") { value(traces.size) }
                jsonPath("$.totalPages") { value(traces.size / pageSize + 1) }
                jsonPath("$.content[0].id") { value(traces[0].id) }
                jsonPath("$.content[0].value") { value(traces[0].value) }
                jsonPath("$.content[0].application") { value(traces[0].application) }
                jsonPath("$.content[0].generatedAt") { value(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(traces[0].generatedAt)) }
                jsonPath("$.content[0].labels[0].id") { value(traces[0].labels[0].id) }
                jsonPath("$.content[0].labels[0].label") { value(traces[0].labels[0].label) }
                jsonPath("$.content[0].labels[1].id") { value(traces[0].labels[1].id) }
                jsonPath("$.content[0].labels[1].label") { value(traces[0].labels[1].label) }
                jsonPath("$.content[1].id") { value(traces[1].id) }
                jsonPath("$.content[1].value") { value(traces[1].value) }
                jsonPath("$.content[1].application") { value(traces[1].application) }
                jsonPath("$.content[1].generatedAt") { value(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(traces[1].generatedAt)) }
                jsonPath("$.content[1].labels[0].id") { value(traces[1].labels[0].id) }
                jsonPath("$.content[1].labels[0].label") { value(traces[1].labels[0].label) }
            }
        verify { traceService.findAll(traceFilter, pageable) }
    }

}