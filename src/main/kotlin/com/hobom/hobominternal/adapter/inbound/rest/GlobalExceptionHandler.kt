package com.hobom.hobominternal.adapter.inbound.rest

import com.hobom.hobominternal.exception.ApplicationException
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

    data class ErrorResponse(
        val status: Int,
        val message: String,
    )

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(ex: ApplicationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(404, ex.message ?: "Not Found"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest()
            .body(ErrorResponse(400, message))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val message = ex.constraintViolations
            .joinToString(", ") { it.message }
        return ResponseEntity.badRequest()
            .body(ErrorResponse(400, message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest()
            .body(ErrorResponse(400, ex.message ?: "Bad Request"))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unhandled exception", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(500, "Internal Server Error"))
    }
}
