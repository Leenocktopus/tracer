package com.leandoer.tracer.model.dto.alertevent

import com.leandoer.tracer.model.entity.AlertStatus

data class PutEventAlertDto(
    val status: AlertStatus
)
