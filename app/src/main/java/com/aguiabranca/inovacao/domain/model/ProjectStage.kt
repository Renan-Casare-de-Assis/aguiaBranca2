package com.aguiabranca.inovacao.domain.model

enum class ProjectStage {
    DISCOVERY,
    PLANNING,
    EXECUTION,
    VALIDATION,
    CLOSED;

    companion object {
        fun fromDb(value: String): ProjectStage = when (value.uppercase()) {
            "DESCOBERTA"   -> DISCOVERY
            "PLANEJAMENTO" -> PLANNING
            "EXECUCAO"     -> EXECUTION
            "VALIDACAO"    -> VALIDATION
            "ENCERRADO"    -> CLOSED
            else           -> DISCOVERY
        }
    }

    fun toDb(): String = when (this) {
        DISCOVERY  -> "DESCOBERTA"
        PLANNING   -> "PLANEJAMENTO"
        EXECUTION  -> "EXECUCAO"
        VALIDATION -> "VALIDACAO"
        CLOSED     -> "ENCERRADO"
    }

    fun displayName(): String = when (this) {
        DISCOVERY  -> "Descoberta"
        PLANNING   -> "Planejamento"
        EXECUTION  -> "Execução"
        VALIDATION -> "Validação"
        CLOSED     -> "Encerrado"
    }
}

