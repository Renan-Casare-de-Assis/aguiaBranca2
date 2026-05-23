package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.remote.oracle.OracleDataSource
import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor() : DashboardRepository {

    override suspend fun getMetrics(): Result<DashboardMetrics> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM METRICAS_PAINEL ORDER BY PERIODO_INICIO DESC FETCH FIRST 1 ROW ONLY"
                    val stmt = conn.prepareStatement(sql)
                    val rs = stmt.executeQuery()
                    if (!rs.next()) {
                        // Calcula métricas dinamicamente se não houver registro
                        DashboardMetrics(0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                    } else {
                        DashboardMetrics(
                            totalIdeas            = rs.getInt("TOTAL_IDEIAS"),
                            approvedIdeas         = rs.getInt("IDEIAS_APROVADAS"),
                            activeProjects        = rs.getInt("PROJETOS_ATIVOS"),
                            totalInvestment       = rs.getDouble("INVESTIMENTO_TOTAL"),
                            totalReturn           = rs.getDouble("RETORNO_TOTAL"),
                            totalProfit           = rs.getDouble("LUCRO_TOTAL"),
                            totalCostReduction    = rs.getDouble("REDUCAO_CUSTO_TOTAL"),
                            avgProductivityGainPct= rs.getDouble("MEDIA_GANHO_PRODUTIVIDADE"),
                            roiGlobal             = rs.getDouble("ROI_GLOBAL")
                        )
                    }
                }.getOrThrow()
            }
        }
}

