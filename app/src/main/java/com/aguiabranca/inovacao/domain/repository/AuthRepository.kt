package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.Session

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Session>
    suspend fun logout()
    fun getSession(): Session?
    fun isLoggedIn(): Boolean
}

