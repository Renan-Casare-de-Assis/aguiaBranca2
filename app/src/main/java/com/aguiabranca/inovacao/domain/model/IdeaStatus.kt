package com.aguiabranca.inovacao.domain.model

enum class IdeaStatus {
    NEW,
    UNDER_REVIEW,
    PRIORITIZED,
    APPROVED,
    REJECTED,
    IN_PROJECT;

    companion object {
        fun fromDb(value: String): IdeaStatus = when (value.uppercase()) {
            "NOVA"        -> NEW
            "EM_REVISAO"  -> UNDER_REVIEW
            "PRIORIZADA"  -> PRIORITIZED
            "APROVADA"    -> APPROVED
            "REJEITADA"   -> REJECTED
            "EM_PROJETO"  -> IN_PROJECT
            else          -> NEW
        }
    }

    fun toDb(): String = when (this) {
        NEW          -> "NOVA"
        UNDER_REVIEW -> "EM_REVISAO"
        PRIORITIZED  -> "PRIORIZADA"
        APPROVED     -> "APROVADA"
        REJECTED     -> "REJEITADA"
        IN_PROJECT   -> "EM_PROJETO"
    }
}
