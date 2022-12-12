package com.leandoer.tracer.model.dto.trace

import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class TraceFilter(
    @field:Size(min = 0, max = 50)
    val applications: List<String> = listOf(),

    @field:Size(min = 0, max = 50)
    var labels: List<String> = listOf(),

    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val startTimestamp: LocalDateTime?,

    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val endTimestamp: LocalDateTime?,

    @field:Min(1)
    val traceId: Long?
)