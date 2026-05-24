package com.aguiabranca.inovacao.domain.usecase.ideas

import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class GetAllIdeasUseCase @Inject constructor(
    private val repository: IdeaRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<Idea>> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.MANAGER)
            return Result.failure(SecurityException("Apenas gestores podem ver todas as ideias"))
        return repository.getAll()
    }
}

