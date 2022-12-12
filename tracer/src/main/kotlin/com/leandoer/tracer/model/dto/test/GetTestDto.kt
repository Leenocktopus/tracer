package com.leandoer.tracer.model.dto.test

import com.leandoer.tracer.model.entity.Package
import com.leandoer.tracer.model.entity.TestType

data class GetTestDto(
    val id: Int,

    val title: String,

    val name: TestType,

    val type: Package
)
