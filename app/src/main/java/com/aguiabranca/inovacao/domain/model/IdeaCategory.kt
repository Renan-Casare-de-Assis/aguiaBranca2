package com.aguiabranca.inovacao.domain.model

enum class IdeaCategory {
    PROCESS,
    COST,
    SAFETY,
    PRODUCTIVITY,
    REVENUE;

    companion object {
        fun fromDb(value: String): IdeaCategory = when (value.uppercase()) {
            "PROCESSO"      -> PROCESS
            "CUSTO"         -> COST
            "SEGURANCA"     -> SAFETY
            "PRODUTIVIDADE" -> PRODUCTIVITY
            "RECEITA"       -> REVENUE
            else            -> PROCESS
        }
    }

    fun toDb(): String = when (this) {
        PROCESS      -> "PROCESSO"
        COST         -> "CUSTO"
        SAFETY       -> "SEGURANCA"
        PRODUCTIVITY -> "PRODUTIVIDADE"
        REVENUE      -> "RECEITA"
    }

    fun displayName(): String = when (this) {
        PROCESS      -> "Processo"
        COST         -> "Custo"
        SAFETY       -> "Segurança"
        PRODUCTIVITY -> "Produtividade"
        REVENUE      -> "Receita"
    }
}

