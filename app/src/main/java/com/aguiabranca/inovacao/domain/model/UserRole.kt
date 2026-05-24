package com.aguiabranca.inovacao.domain.model

fun UserRole.toDb(): String = when (this) {
    UserRole.OPERATOR   -> "OPERADOR"
    UserRole.MANAGER    -> "GERENTE"
    UserRole.LEADERSHIP -> "LIDERANCA"
}
