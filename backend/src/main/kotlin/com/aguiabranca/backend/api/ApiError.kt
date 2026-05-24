package com.aguiabranca.backend.api

data class ApiError(
    val code: String,
    val message: String,
    val details: String? = null
)

