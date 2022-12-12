package com.leandoer.tracer.exception

data class Error(
    val error: String,
    val code: Int,
    val message: String,
    val metadata: Map<String, Any> = mapOf()
)
