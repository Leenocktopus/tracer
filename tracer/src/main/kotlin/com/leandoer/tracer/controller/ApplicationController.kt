package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.application.GetApplicationDto
import com.leandoer.tracer.model.entity.Application_
import com.leandoer.tracer.service.ApplicationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/applications")
class ApplicationController(
    private val applicationService: ApplicationService
) {

    @GetMapping
    fun findAll(
        @PageableDefault(sort = [Application_.ID]) pageable: Pageable
    ): Page<GetApplicationDto> {
        return applicationService.findAll(pageable)
    }
}