package com.aguiabranca.inovacao.domain.usecase.projects

import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String,
        objective: String,
        stage: ProjectStage,
        status: ProjectStatus,
        investment: Double,
        financialReturn: Double,
        costReduction: Double,
        productivityGainPct: Double,
        progressPct: Int
    ): Result<Project> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.MANAGER)
            return Result.failure(SecurityException("Apenas gestores podem atualizar projetos"))
        return repository.update(id, title.trim(), objective.trim(), stage, status,
            investment, financialReturn, costReduction, productivityGainPct, progressPct)
    }
}

