package com.leandoer.tracer.model.dto.analytics

import com.leandoer.tracer.model.entity.Intervals

data class RateFilter(

    val type: TraceRateType,

    val interval: Intervals

)