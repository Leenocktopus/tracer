package com.leandoer.tracer.model.dto.analytics

import com.fasterxml.jackson.annotation.JsonFormat
import com.leandoer.tracer.configuration.DATE_TIME_FORMAT
import java.time.LocalDateTime

data class RateDto(
    val label: String,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    val timestamp: LocalDateTime,

    val count: Int
)