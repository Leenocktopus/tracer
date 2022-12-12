package com.leandoer.tracer.model.dto.trace

import java.time.LocalDateTime

data class PostTraceDto(
    val value: String,
    val type: PayloadType,
    val application: String,
    val labels: List<String>,
    val generatedAt: LocalDateTime
)