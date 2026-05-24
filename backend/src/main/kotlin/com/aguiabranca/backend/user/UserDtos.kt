package com.aguiabranca.backend.user

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val unit: String?,
    val avatarUrl: String?,
    val createdAt: Long
)

data class LoginRequest(
    val email: String,
    val password: String
)

