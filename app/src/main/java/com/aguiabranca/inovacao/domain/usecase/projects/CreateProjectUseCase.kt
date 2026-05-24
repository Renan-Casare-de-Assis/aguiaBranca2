package com.aguiabranca.inovacao.domain.usecase.projects

import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        ideaId: String?,
        title: String,
        objective: String,
        startDate: Long,
        targetEndDate: Long,
        investment: Double
    ): Result<Project> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.MANAGER)
            return Result.failure(SecurityException("Apenas gestores podem criar projetos"))
        if (title.isBlank()) return Result.failure(Exception("Título não pode estar vazio"))
        if (objective.isBlank()) return Result.failure(Exception("Objetivo não pode estar vazio"))
        return repository.create(
            ideaId, title.trim(), objective.trim(),
            ProjectStage.DISCOVERY, ProjectStatus.ON_TRACK,
            session.user.id, startDate, targetEndDate, investment
        )
    }
}

