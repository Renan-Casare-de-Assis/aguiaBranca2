package com.aguiabranca.inovacao.domain.model

enum class IdeaPriority {
    LOW,
    MEDIUM,
    HIGH;

    companion object {
        fun fromDb(value: String?): IdeaPriority? = when (value?.uppercase()) {
            "BAIXA" -> LOW
            "MEDIA" -> MEDIUM
            "ALTA"  -> HIGH
            else    -> null
        }
    }

    fun toDb(): String = when (this) {
        LOW    -> "BAIXA"
        MEDIUM -> "MEDIA"
        HIGH   -> "ALTA"
    }

    fun displayName(): String = when (this) {
        LOW    -> "Baixa"
        MEDIUM -> "Média"
        HIGH   -> "Alta"
    }
}

