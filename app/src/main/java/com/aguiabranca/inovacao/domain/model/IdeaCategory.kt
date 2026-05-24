package com.aguiabranca.inovacao.domain.model

enum class IdeaCategory {
    PROCESS,
    TECHNOLOGY,
    PEOPLE,
    SUSTAINABILITY,
    COST,
    OTHER,
    SAFETY,
    PRODUCTIVITY,
    REVENUE;

    companion object {
        fun fromDb(value: String): IdeaCategory = when (value.uppercase()) {
            "PROCESSO"      -> PROCESS
            "TECNOLOGIA"    -> TECHNOLOGY
            "PESSOAS"       -> PEOPLE
            "SUSTENTABILIDADE" -> SUSTAINABILITY
            "CUSTO"         -> COST
            "OUTRO"         -> OTHER
            "SEGURANCA"     -> SAFETY
            "PRODUTIVIDADE" -> PRODUCTIVITY
            "RECEITA"       -> REVENUE
            else            -> PROCESS
        }
    }

    fun toDb(): String = when (this) {
        PROCESS      -> "PROCESSO"
        TECHNOLOGY   -> "TECNOLOGIA"
        PEOPLE       -> "PESSOAS"
        SUSTAINABILITY -> "SUSTENTABILIDADE"
        COST         -> "CUSTO"
        OTHER        -> "OUTRO"
        SAFETY       -> "SEGURANCA"
        PRODUCTIVITY -> "PRODUTIVIDADE"
        REVENUE      -> "RECEITA"
    }

    fun displayName(): String = when (this) {
        PROCESS      -> "Processo"
        TECHNOLOGY   -> "Tecnologia"
        PEOPLE       -> "Pessoas"
        SUSTAINABILITY -> "Sustentabilidade"
        COST         -> "Custo"
        OTHER        -> "Outro"
        SAFETY       -> "Segurança"
        PRODUCTIVITY -> "Produtividade"
        REVENUE      -> "Receita"
    }
}

