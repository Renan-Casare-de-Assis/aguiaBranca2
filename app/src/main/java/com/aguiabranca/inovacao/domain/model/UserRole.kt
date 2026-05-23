package com.aguiabranca.inovacao.domain.model

enum class UserRole {
    OPERATOR,
    MANAGER,
    LEADERSHIP;

    companion object {
        fun fromDb(value: String): UserRole = when (value.uppercase()) {
            "OPERADOR"  -> OPERATOR
            "GERENTE"   -> MANAGER
            "LIDERANCA" -> LEADERSHIP
            else        -> OPERATOR
        }
    }

    fun toDb(): String = when (this) {
        OPERATOR   -> "OPERADOR"
        MANAGER    -> "GERENTE"
        LEADERSHIP -> "LIDERANCA"
    }
}

