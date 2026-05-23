package com.aguiabranca.inovacao.domain.usecase.dashboard

import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.DashboardRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class GetDashboardMetricsUseCase @Inject constructor(
    private val repository: DashboardRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<DashboardMetrics> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.LEADERSHIP)
            return Result.failure(SecurityException("Apenas líderes podem acessar o dashboard"))
        return repository.getMetrics()
    }
}

