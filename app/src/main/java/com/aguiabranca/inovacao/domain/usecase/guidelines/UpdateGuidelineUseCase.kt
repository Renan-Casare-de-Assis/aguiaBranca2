package com.aguiabranca.inovacao.domain.usecase.guidelines

import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.GuidelineStatus
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateGuidelineUseCase @Inject constructor(
    private val repository: GuidelineRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String, title: String, description: String, pillar: String, status: GuidelineStatus): Result<Guideline> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.LEADERSHIP)
            return Result.failure(SecurityException("Apenas líderes podem editar orientações"))
        return repository.update(id, title.trim(), description.trim(), pillar)
    }
}

