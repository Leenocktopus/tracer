package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.testrun.GetTestRunDto
import com.leandoer.tracer.model.dto.testrun.TestRunFilter
import com.leandoer.tracer.service.TestRunService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/test-runs")
class TestRunController(
    private val testRunService: TestRunService
) {

    @GetMapping
    fun findAll(
        @Valid testRunFilter: TestRunFilter,
        @PageableDefault pageable: Pageable
    ): Page<GetTestRunDto> {
        return testRunService.findAll(testRunFilter, pageable)
    }
}