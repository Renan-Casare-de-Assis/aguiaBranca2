package com.aguiabranca.inovacao.domain.usecase.auth

import com.aguiabranca.inovacao.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}

