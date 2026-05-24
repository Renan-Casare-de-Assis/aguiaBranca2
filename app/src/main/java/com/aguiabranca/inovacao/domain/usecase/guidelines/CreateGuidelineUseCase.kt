package com.aguiabranca.inovacao.domain.usecase.guidelines

import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class CreateGuidelineUseCase @Inject constructor(
    private val repository: GuidelineRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(title: String, description: String, pillar: String): Result<Guideline> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.LEADERSHIP)
            return Result.failure(SecurityException("Apenas líderes podem criar orientações"))
        if (title.isBlank()) return Result.failure(Exception("Título não pode estar vazio"))
        if (description.isBlank()) return Result.failure(Exception("Descrição não pode estar vazia"))
        return repository.create(title.trim(), description.trim(), pillar, session.user.id)
    }
}

