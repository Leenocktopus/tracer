package com.leandoer.tracer.model.dto.alert

import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class PutAlertDto(

    @field:Valid
    @field:Size(min = 1)
    val contacts: List<AlertContactDto>,

    val labels: List<Int>
) {
    data class AlertContactDto(
        @field:Size(min = 1, max = 100)
        @field:Email
        val email: String
    )
}