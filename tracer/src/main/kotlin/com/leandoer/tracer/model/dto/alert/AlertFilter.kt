package com.leandoer.tracer.model.dto.alert

import javax.validation.constraints.Min

data class AlertFilter(
    @field:Min(1)
    val labelId: Int?

)