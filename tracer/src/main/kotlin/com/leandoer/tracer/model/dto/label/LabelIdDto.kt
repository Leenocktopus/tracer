package com.leandoer.tracer.model.dto.label

import javax.validation.constraints.Min


data class LabelIdDto(
    @Min(1)
    val id: String
)