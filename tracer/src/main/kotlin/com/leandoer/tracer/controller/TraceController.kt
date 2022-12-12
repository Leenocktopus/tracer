package com.leandoer.tracer.controller

import com.leandoer.tracer.model.dto.trace.GetTraceDto
import com.leandoer.tracer.model.dto.trace.TraceFilter
import com.leandoer.tracer.model.dto.trace.PostTraceDto
import com.leandoer.tracer.model.entity.Trace_
import com.leandoer.tracer.service.TraceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.CREATED
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/traces")
class TraceController(
    private val traceService: TraceService
) {

    @GetMapping
    fun findAll(
        @Valid traceFilter: TraceFilter,
        @PageableDefault(sort = [Trace_.GENERATED_AT], direction = DESC) pageable: Pageable
    ): Page<GetTraceDto> {
        return traceService.findAll(traceFilter, pageable)
    }

    @PostMapping
    @ResponseStatus(CREATED)
    fun processSubmission(@RequestBody @Valid trace: PostTraceDto) {
        traceService.create(trace)
    }
}