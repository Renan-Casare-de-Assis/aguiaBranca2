package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.core.session.SessionManager
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.local.room.entity.UserEntity
import com.aguiabranca.inovacao.data.remote.api.LoginRequestDto
import com.aguiabranca.inovacao.data.remote.api.UserApiService
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.Session
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.model.toDb
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val userDao: UserDao,
    private val userApi: UserApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Session> =
        withContext(Dispatchers.IO) {
            runCatching {
                val user: User = userApi.login(LoginRequestDto(email, password)).toDomain()
                // Cache local
                userDao.insert(UserEntity(user.id, user.name, user.email, user.role.toDb(), user.unit, true))
                // Salva sessão
                sessionManager.saveSession(user)
                Session(user)
            }
        }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun getSession(): Session? = sessionManager.getSession()

    override fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
}

