package com.aguiabranca.inovacao.data.repository

import android.util.Log
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.remote.api.LoginRequestDto
import com.aguiabranca.inovacao.data.remote.api.UserApiService
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApiService
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                val normalizedEmail = email.trim().lowercase()
                val normalizedPassword = password.trim()

                Log.d(TAG, "Tentando login via API para $normalizedEmail")
                userApi.login(LoginRequestDto(normalizedEmail, normalizedPassword)).toDomain()
            }
        }

    override suspend fun getById(id: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                userApi.getById(id).toDomain()
            }
        }

    override suspend fun getAll(): Result<List<User>> =
        withContext(Dispatchers.IO) {
            runCatching {
                userApi.getAll().map { it.toDomain() }
            }
        }
}

