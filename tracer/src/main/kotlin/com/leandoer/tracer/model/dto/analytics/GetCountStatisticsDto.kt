package com.leandoer.tracer.model.dto.analytics

data class GetCountStatisticsDto(
    val applications: Long,
    val labels: Long,
    val traces: Long,
    val alerts: Long,
    val alertEvents: Long,
    val alertEventsNew: Long,
    val alertEventsResolved: Long
)