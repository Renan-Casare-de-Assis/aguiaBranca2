package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.remote.api.DashboardApiService
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApiService
) : DashboardRepository {

    override suspend fun getMetrics(): Result<DashboardMetrics> =
        withContext(Dispatchers.IO) {
            runCatching {
                dashboardApi.getMetrics().toDomain()
            }
        }
}

