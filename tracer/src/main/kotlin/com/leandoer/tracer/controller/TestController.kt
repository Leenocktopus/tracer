package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.test.GetTestDto
import com.leandoer.tracer.model.dto.test.TestFilter
import com.leandoer.tracer.model.entity.Test_
import com.leandoer.tracer.service.TestService
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
@RequestMapping("/tests")
class TestController(
    private val testService: TestService
) {

    @GetMapping
    fun findAll(
        @Valid testFilter: TestFilter,
        @PageableDefault(sort = [Test_.ID]) pageable: Pageable
    ): Page<GetTestDto> {
        return testService.findAll(testFilter, pageable)
    }
}