package com.aguiabranca.backend.dashboard

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
open class DashboardRepository(private val jdbcTemplate: JdbcTemplate) {

    open fun metrics(): DashboardMetricsResponse {
        val fromMetricsTable = jdbcTemplate.query(
            "SELECT * FROM METRICAS_PAINEL ORDER BY PERIODO_INICIO DESC FETCH FIRST 1 ROW ONLY"
        ) { rs, _ ->
            DashboardMetricsResponse(
                totalIdeas = rs.getInt("TOTAL_IDEIAS"),
                approvedIdeas = rs.getInt("IDEIAS_APROVADAS"),
                activeProjects = rs.getInt("PROJETOS_ATIVOS"),
                totalInvestment = rs.getDouble("INVESTIMENTO_TOTAL"),
                totalReturn = rs.getDouble("RETORNO_TOTAL"),
                totalProfit = rs.getDouble("LUCRO_TOTAL"),
                totalCostReduction = rs.getDouble("REDUCAO_CUSTO_TOTAL"),
                avgProductivityGainPct = rs.getDouble("MEDIA_GANHO_PRODUTIVIDADE"),
                roiGlobal = rs.getDouble("ROI_GLOBAL")
            )
        }.firstOrNull()

        if (fromMetricsTable != null) return fromMetricsTable

        val totalIdeas = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM IDEIAS", Int::class.java) ?: 0
        val approvedIdeas = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM IDEIAS WHERE STATUS = 'APROVADA'", Int::class.java) ?: 0
        val activeProjects = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PROJETOS WHERE STATUS IN ('ATIVO', 'ON_TRACK', 'NO_CRONOGRAMA')", Int::class.java) ?: 0

        val financial = jdbcTemplate.query(
            """
            SELECT
                SUM(INVESTIMENTO) AS TOTAL_INV,
                SUM(RETORNO_FINANCEIRO) AS TOTAL_RET,
                SUM(REDUCAO_CUSTO) AS TOTAL_RED,
                AVG(GANHO_PRODUTIVIDADE_PCT) AS AVG_PROD,
                AVG(ROI) AS AVG_ROI
            FROM PROJETOS
            """.trimIndent()
        ) { rs, _ ->
            mapOf(
                "totalInv" to (rs.getDouble("TOTAL_INV").takeUnless { rs.wasNull() } ?: 0.0),
                "totalRet" to (rs.getDouble("TOTAL_RET").takeUnless { rs.wasNull() } ?: 0.0),
                "totalRed" to (rs.getDouble("TOTAL_RED").takeUnless { rs.wasNull() } ?: 0.0),
                "avgProd" to (rs.getDouble("AVG_PROD").takeUnless { rs.wasNull() } ?: 0.0),
                "avgRoi" to (rs.getDouble("AVG_ROI").takeUnless { rs.wasNull() } ?: 0.0)
            )
        }.firstOrNull() ?: emptyMap()

        val totalInvestment = financial["totalInv"] ?: 0.0
        val totalReturn = financial["totalRet"] ?: 0.0

        return DashboardMetricsResponse(
            totalIdeas = totalIdeas,
            approvedIdeas = approvedIdeas,
            activeProjects = activeProjects,
            totalInvestment = totalInvestment,
            totalReturn = totalReturn,
            totalProfit = totalReturn - totalInvestment,
            totalCostReduction = financial["totalRed"] ?: 0.0,
            avgProductivityGainPct = financial["avgProd"] ?: 0.0,
            roiGlobal = financial["avgRoi"] ?: 0.0
        )
    }
}

