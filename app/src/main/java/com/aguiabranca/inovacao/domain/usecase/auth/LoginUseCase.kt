package com.aguiabranca.inovacao.domain.usecase.auth

import com.aguiabranca.inovacao.domain.model.Session
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Session> {
        if (email.isBlank()) return Result.failure(Exception("Email não pode estar vazio"))
        if (password.length < 6) return Result.failure(Exception("Senha deve ter no mínimo 6 caracteres"))
        return authRepository.login(email.trim(), password)
    }
}

