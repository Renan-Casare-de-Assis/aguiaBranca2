package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.DashboardMetrics

interface DashboardRepository {
    suspend fun getMetrics(): Result<DashboardMetrics>
}

