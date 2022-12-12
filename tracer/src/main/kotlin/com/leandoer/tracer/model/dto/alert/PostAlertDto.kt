package com.leandoer.tracer.model.dto.alert

import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class PostAlertDto(
    @field:Size(min = 1, max = 50)
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$")
    val name: String,

    @field:Valid
    @field:Size(min = 1)
    val contacts: List<AlertContactDto>
) {
    data class AlertContactDto(
        @field:Size(min = 1, max = 100)
        @field:Email
        val email: String
    )
}
