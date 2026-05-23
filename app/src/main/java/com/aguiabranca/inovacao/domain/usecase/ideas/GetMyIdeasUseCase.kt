package com.aguiabranca.inovacao.domain.usecase.ideas

import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class GetMyIdeasUseCase @Inject constructor(
    private val repository: IdeaRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<Idea>> {
        val session = authRepository.getSession()
            ?: return Result.failure(Exception("Usuário não autenticado"))
        return repository.getMyIdeas(session.user.uid)
    }
}

