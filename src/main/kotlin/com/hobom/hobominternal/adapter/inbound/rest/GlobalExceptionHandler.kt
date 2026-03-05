package com.hobom.hobominternal.adapter.inbound.rest

import com.hobom.hobominternal.exception.ApplicationException
import com.hobom.hobominternal.shared.response.HttpResponse
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(ex: ApplicationException): ResponseEntity<HttpResponse<Nothing?>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(HttpResponse.error(ex.message ?: "Not Found"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<HttpResponse<Nothing?>> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest()
            .body(HttpResponse.error(message))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<HttpResponse<Nothing?>> {
        val message = ex.constraintViolations
            .joinToString(", ") { it.message }
        return ResponseEntity.badRequest()
            .body(HttpResponse.error(message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<HttpResponse<Nothing?>> {
        return ResponseEntity.badRequest()
            .body(HttpResponse.error(ex.message ?: "Bad Request"))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ResponseEntity<HttpResponse<Nothing?>> {
        log.error("Unhandled exception", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(HttpResponse.error("Internal Server Error"))
    }
}
