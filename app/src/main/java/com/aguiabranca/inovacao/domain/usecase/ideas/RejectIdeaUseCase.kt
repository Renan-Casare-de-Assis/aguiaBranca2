package com.aguiabranca.inovacao.domain.usecase.ideas

import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class RejectIdeaUseCase @Inject constructor(
    private val repository: IdeaRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(ideaId: String, comment: String): Result<Unit> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        if (session.user.role != UserRole.MANAGER)
            return Result.failure(SecurityException("Apenas gestores podem rejeitar ideias"))
        if (comment.isBlank())
            return Result.failure(Exception("Comentário é obrigatório ao rejeitar uma ideia"))
        return repository.updateStatus(ideaId, IdeaStatus.REJECTED, comment)
    }
}

