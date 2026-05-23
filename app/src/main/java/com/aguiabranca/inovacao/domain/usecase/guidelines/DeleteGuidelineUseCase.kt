package com.aguiabranca.inovacao.domain.usecase.guidelines

import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class DeleteGuidelineUseCase @Inject constructor(
    private val repository: GuidelineRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.LEADERSHIP)
            return Result.failure(SecurityException("Apenas líderes podem excluir orientações"))
        return repository.delete(id)
    }
}

