package com.leandoer.tracer.model.dto.trace

import com.fasterxml.jackson.annotation.JsonFormat
import com.leandoer.tracer.configuration.DATE_TIME_FORMAT
import com.leandoer.tracer.model.dto.label.GetLabelDto
import java.time.LocalDateTime

data class GetTraceDto(
    val id: Long,

    val value: String,

    val application: String,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    val generatedAt: LocalDateTime,

    var labels: List<TraceLabelDto> = listOf()
){
    data class TraceLabelDto(
        val id: Int,
        val label: String
    )
}
