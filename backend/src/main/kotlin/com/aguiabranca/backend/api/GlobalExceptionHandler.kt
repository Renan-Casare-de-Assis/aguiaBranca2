package com.aguiabranca.backend.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.CannotGetJdbcConnectionException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiError("BAD_REQUEST", ex.message ?: "Requisição inválida"))

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiError("NOT_FOUND", ex.message ?: "Recurso não encontrado"))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val firstError = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
            ?: "Dados inválidos"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiError("VALIDATION_ERROR", firstError))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiError("DATA_INTEGRITY", "Dados inválidos para persistência", ex.mostSpecificCause?.message ?: ex.message))

    @ExceptionHandler(CannotGetJdbcConnectionException::class)
    fun handleDbConnection(ex: CannotGetJdbcConnectionException): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(ApiError("DB_UNAVAILABLE", "Banco de dados indisponível no momento", ex.mostSpecificCause?.message ?: ex.message))

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError("INTERNAL_ERROR", "Erro interno ao processar requisição", ex.message))
}

