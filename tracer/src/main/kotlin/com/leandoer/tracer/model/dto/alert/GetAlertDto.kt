package com.leandoer.tracer.model.dto.alert

data class GetAlertDto(
    val id: Int,
    val name: String,
    val contacts: List<AlertContactDto>,
    val labels: List<AlertLabelDto>
) {
    data class AlertLabelDto(
        val id: Int,
        val label: String
    )

    data class AlertContactDto(
        val id: Int,
        val email: String
    )
}
