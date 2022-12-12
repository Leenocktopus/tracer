package com.leandoer.tracer.model.dto.alertevent

import javax.validation.constraints.Min

data class AlertEventFilter(
    @field:Min(1)
    val alertId: Int?
)