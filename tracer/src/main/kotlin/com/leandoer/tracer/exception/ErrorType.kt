package com.leandoer.tracer.exception


import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.HttpStatus.NOT_ACCEPTABLE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE

enum class ErrorType(
    val code: Int, val message: String, val statusCode: HttpStatus
) {
    NOT_FOUND_EXCEPTION(1, "entity not found", NOT_FOUND),
    CONSTRAINT_VIOLATION_EXCEPTION(2, "API usage violation", BAD_REQUEST),
    INTERNAL_SERVER_ERROR(3, "internal error", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_EXCEPTION(4, "bad request", BAD_REQUEST),
    NO_HANDLER_FOUND_EXCEPTION(5, "no handler found", NOT_FOUND),
    REQUEST_METHOD_NOT_SUPPORTED(6, "request method not supported", METHOD_NOT_ALLOWED),
    MEDIA_TYPE_NOT_SUPPORTED(7, "media type not supported", UNSUPPORTED_MEDIA_TYPE),
    MEDIA_TYPE_NOT_ACCEPTABLE(8, "media type not acceptable", NOT_ACCEPTABLE),
    INVALID_FORMAT_EXCEPTION(13, "error during serialization", BAD_REQUEST),
    MESSAGE_NOT_READABLE_EXCEPTION(14, "message not readable", BAD_REQUEST),


}