package com.leandoer.tracer.model.dto.analytics

import com.leandoer.tracer.model.entity.Package

data class GetTestStatisticsDto(
    val testType: String,
    val type: Package,
    val count: Int
)