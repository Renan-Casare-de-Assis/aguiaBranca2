package com.aguiabranca.inovacao.domain.model

enum class GuidelineStatus {
    ACTIVE,
    INACTIVE;

    companion object {
        fun fromDb(value: String): GuidelineStatus = when (value.uppercase()) {
            "ATIVO"   -> ACTIVE
            "INATIVO" -> INACTIVE
            else      -> INACTIVE
        }
    }

    fun toDb(): String = when (this) {
        ACTIVE   -> "ATIVO"
        INACTIVE -> "INATIVO"
    }
}

