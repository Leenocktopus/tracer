package com.leandoer.tracer.model.dto.label

import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


data class PostLabelDto(
    @field:Size(min = 1, max = 50)
    @field:Pattern(regexp = "^[a-zA-Z0-9]+\$")
    val label: String
)