package com.aguiabranca.inovacao.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val unit: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Long = 0L
)

enum class UserRole {
    OPERATOR, MANAGER, LEADERSHIP;

    companion object {
        fun fromDb(value: String): UserRole = when (value.uppercase()) {
            "OPERATOR", "OPERADOR"     -> OPERATOR
            "MANAGER",  "GESTOR"       -> MANAGER
            "LEADERSHIP","LIDERANCA",
            "LIDERANÇA"                -> LEADERSHIP
            else                       -> OPERATOR
        }
    }
}
