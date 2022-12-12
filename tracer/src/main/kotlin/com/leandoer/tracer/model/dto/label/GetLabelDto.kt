package com.leandoer.tracer.model.dto.label

import com.leandoer.tracer.model.entity.Package
import com.leandoer.tracer.model.entity.TestType


data class GetLabelDto(
    val id: Int,
    val label: String,
    val tests: List<LabelTestDto> = listOf(),
    val alerts: List<LabelAlertDto> = listOf()

) {
    data class LabelAlertDto(
        val id: Int,
        val name: String
    )

    data class LabelTestDto(
        val id: Int,
        val title: String,
        val name: TestType,
        val type: Package
    )
}