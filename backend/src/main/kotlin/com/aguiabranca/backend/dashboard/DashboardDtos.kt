package com.aguiabranca.backend.dashboard

data class DashboardMetricsResponse(
    val totalIdeas: Int,
    val approvedIdeas: Int,
    val activeProjects: Int,
    val totalInvestment: Double,
    val totalReturn: Double,
    val totalProfit: Double,
    val totalCostReduction: Double,
    val avgProductivityGainPct: Double,
    val roiGlobal: Double
)

