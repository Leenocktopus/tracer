package com.leandoer.tracer.model.dto.testrun

import java.math.BigDecimal

data class GetTestRunDto(
    val id: Long,

    val test: String,

    val testResult: BigDecimal,

    val random: Boolean?
)
