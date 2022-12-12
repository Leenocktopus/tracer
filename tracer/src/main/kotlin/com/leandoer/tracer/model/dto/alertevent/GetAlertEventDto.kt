package com.leandoer.tracer.model.dto.alertevent

import com.fasterxml.jackson.annotation.JsonFormat
import com.leandoer.tracer.configuration.DATE_TIME_FORMAT
import com.leandoer.tracer.model.entity.AlertStatus
import java.time.LocalDateTime

data class GetAlertEventDto(
    val id: Long,

    val reason: String,

    val traceId: Long,

    val status: AlertStatus,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    val generatedAt: LocalDateTime
)