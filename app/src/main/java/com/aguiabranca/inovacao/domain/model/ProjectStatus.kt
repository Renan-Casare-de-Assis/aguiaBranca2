package com.aguiabranca.inovacao.domain.model

enum class ProjectStatus {
    ON_TRACK,
    AT_RISK,
    DELAYED,
    COMPLETED,
    CANCELED;

    companion object {
        fun fromDb(value: String): ProjectStatus = when (value.uppercase()) {
            "NO_CRONOGRAMA" -> ON_TRACK
            "EM_RISCO"      -> AT_RISK
            "ATRASADO"      -> DELAYED
            "CONCLUIDO"     -> COMPLETED
            "CANCELADO"     -> CANCELED
            else            -> ON_TRACK
        }
    }

    fun toDb(): String = when (this) {
        ON_TRACK  -> "NO_CRONOGRAMA"
        AT_RISK   -> "EM_RISCO"
        DELAYED   -> "ATRASADO"
        COMPLETED -> "CONCLUIDO"
        CANCELED  -> "CANCELADO"
    }

    fun displayName(): String = when (this) {
        ON_TRACK  -> "No prazo"
        AT_RISK   -> "Em risco"
        DELAYED   -> "Atrasado"
        COMPLETED -> "Concluído"
        CANCELED  -> "Cancelado"
    }
}
