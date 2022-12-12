package com.leandoer.tracer.exception

class ApiException(
    val error: ErrorType,
    val metadata: Map<String, Any>,
    message: String? = null
) : RuntimeException(message ?: error.message) {

}