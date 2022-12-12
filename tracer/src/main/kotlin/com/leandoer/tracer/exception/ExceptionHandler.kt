package com.leandoer.tracer.exception

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.leandoer.tracer.exception.ErrorType.MEDIA_TYPE_NOT_ACCEPTABLE
import com.leandoer.tracer.exception.ErrorType.MEDIA_TYPE_NOT_SUPPORTED
import com.leandoer.tracer.exception.ErrorType.MESSAGE_NOT_READABLE_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.NO_HANDLER_FOUND_EXCEPTION
import com.leandoer.tracer.exception.ErrorType.REQUEST_METHOD_NOT_SUPPORTED
import com.leandoer.tracer.exception.ErrorType.VALIDATION_EXCEPTION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handle(exception: ApiException): ResponseEntity<Error> {
        return ResponseEntity
            .status(exception.error.statusCode)
            .body(
                Error(
                    error = exception.error.name,
                    code = exception.error.code,
                    message = exception.message!!,
                    metadata = exception.metadata
                )
            )
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handle(exception: NoHandlerFoundException): ResponseEntity<Error> {
        return ResponseEntity
            .status(NO_HANDLER_FOUND_EXCEPTION.statusCode)
            .body(
                Error(
                    error = NO_HANDLER_FOUND_EXCEPTION.name,
                    code = NO_HANDLER_FOUND_EXCEPTION.code,
                    message = exception.message!!
                )
            )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(exception: HttpRequestMethodNotSupportedException): ResponseEntity<Error> {
        return ResponseEntity
            .status(REQUEST_METHOD_NOT_SUPPORTED.statusCode)
            .body(
                Error(
                    error = REQUEST_METHOD_NOT_SUPPORTED.name,
                    code = REQUEST_METHOD_NOT_SUPPORTED.code,
                    message = exception.message!!
                )
            )
    }

    @ExceptionHandler(NumberFormatException::class)
    fun handle(exception: NumberFormatException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = "exception during number parse or conversion",
                    metadata = mapOf(
                        "message" to (exception.message ?: "null")
                    )
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                exception.bindingResult.fieldError?.let {
                    Error(
                        error = VALIDATION_EXCEPTION.name,
                        code = VALIDATION_EXCEPTION.code,
                        message = it.defaultMessage ?: "",
                        metadata = mapOf(
                            "field" to it.field,
                            "value" to (it.rejectedValue ?: "null")
                        )
                    )
                } ?: Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = VALIDATION_EXCEPTION.message
                )
            )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(exception: ConstraintViolationException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                exception.constraintViolations.firstOrNull()?.let {
                    Error(
                        error = VALIDATION_EXCEPTION.name,
                        code = VALIDATION_EXCEPTION.code,
                        message = it.message ?: "",
                        metadata = mapOf(
                            "field" to it.propertyPath.toString(),
                            "value" to (it.invalidValue ?: "null")
                        )
                    )
                } ?: Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = VALIDATION_EXCEPTION.message
                )
            )
    }

    @ExceptionHandler(BindException::class)
    fun handle(exception: BindException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                exception.fieldErrors.firstOrNull()?.let {
                    Error(
                        error = VALIDATION_EXCEPTION.name,
                        code = VALIDATION_EXCEPTION.code,
                        message = it.defaultMessage ?: "",
                        metadata = mapOf(
                            "field" to it.field,
                            "value" to (it.rejectedValue ?: "null")
                        )
                    )
                } ?: Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = VALIDATION_EXCEPTION.message
                )
            )
    }


    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(exception: HttpMessageNotReadableException): ResponseEntity<Error> {
        return when (exception.cause) {
            is InvalidFormatException -> return handle(exception.cause as InvalidFormatException)
            is MissingKotlinParameterException -> return handleMissingKotlinParameter(exception.cause as JsonMappingException)
            is MismatchedInputException -> return handleMismatchedInput(exception.cause as JsonMappingException)
            is JsonMappingException -> return handle(exception.cause as JsonMappingException)
            else -> ResponseEntity
                .status(MESSAGE_NOT_READABLE_EXCEPTION.statusCode)
                .body(
                    Error(
                        error = MESSAGE_NOT_READABLE_EXCEPTION.name,
                        code = MESSAGE_NOT_READABLE_EXCEPTION.code,
                        message = MESSAGE_NOT_READABLE_EXCEPTION.message
                    )
                )
        }
    }


    fun handle(exception: InvalidFormatException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = "invalid value for field, must be of different type",
                    metadata = mapOf(
                        "field" to getFieldName(exception),
                        "value" to exception.value,
                        "targetType" to exception.targetType.simpleName
                    )
                )
            )
    }

    fun handleMissingKotlinParameter(exception: JsonMappingException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = "mapping exception for field",
                    metadata = mapOf(
                        "field" to getFieldName(exception),
                        "message" to "must not be null"
                    )
                )
            )
    }

    fun handleMismatchedInput(exception: JsonMappingException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                if ("FAIL_ON_NULL_FOR_PRIMITIVES" in exception.originalMessage) {
                    Error(
                        error
                        = VALIDATION_EXCEPTION.name,
                        code = VALIDATION_EXCEPTION.code,
                        message = "mapping exception for field",
                        metadata = mapOf(
                            "field" to getFieldName(exception),
                            "message" to "must not be null"
                        )
                    )
                } else {
                    Error(
                        error = MESSAGE_NOT_READABLE_EXCEPTION.name,
                        code = MESSAGE_NOT_READABLE_EXCEPTION.code,
                        message = "mismatched input exception"
                    )
                }
            )
    }

    fun handle(exception: JsonMappingException): ResponseEntity<Error> {
        return ResponseEntity
            .status(VALIDATION_EXCEPTION.statusCode)
            .body(
                Error(
                    error = VALIDATION_EXCEPTION.name,
                    code = VALIDATION_EXCEPTION.code,
                    message = "mapping exception for field",
                    metadata = mapOf(
                        "field" to getFieldName(exception),
                        "message" to exception.originalMessage
                    )
                )
            )
    }


    private fun <T : JsonMappingException> getFieldName(cause: T): String {
        return cause.path
            .map { it.fieldName }
            .filterNotNull()
            .joinToString(separator = ".")
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handle(exception: HttpMediaTypeNotSupportedException): ResponseEntity<Error> {
        return ResponseEntity
            .status(MEDIA_TYPE_NOT_SUPPORTED.statusCode)
            .body(
                Error(
                    error = MEDIA_TYPE_NOT_SUPPORTED.name,
                    code = MEDIA_TYPE_NOT_SUPPORTED.code,
                    message = MEDIA_TYPE_NOT_SUPPORTED.message
                )
            )
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun handle(exception: HttpMediaTypeNotAcceptableException): ResponseEntity<Error> {
        return ResponseEntity
            .status(MEDIA_TYPE_NOT_ACCEPTABLE.statusCode)
            .contentType(APPLICATION_JSON)
            .body(
                Error(
                    error = MEDIA_TYPE_NOT_ACCEPTABLE.name,
                    code = MEDIA_TYPE_NOT_ACCEPTABLE.code,
                    message = MEDIA_TYPE_NOT_ACCEPTABLE.message
                )
            )
    }


}
