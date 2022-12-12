package com.leandoer.tracer.repository.projection

import java.time.LocalDateTime

interface Rate {
    val label: String
    val timestamp: LocalDateTime
    val count: Int
}