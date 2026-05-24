package com.aguiabranca.inovacao.data.repository

import android.util.Log
import android.content.Context
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.remote.api.LoginRequestDto
import com.aguiabranca.inovacao.data.remote.api.UserApiService
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApiService,
    @ApplicationContext private val context: Context
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
        private const val TEST_PASSWORD = "070589"
    }

    override suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                val normalizedEmail = email.trim().lowercase()
                val normalizedPassword = password.trim()

                try {
                    Log.d(TAG, "Tentando login via API para $normalizedEmail")
                    userApi.login(LoginRequestDto(normalizedEmail, normalizedPassword)).toDomain()
                } catch (remoteError: Throwable) {
                    Log.e(TAG, "Falha no login remoto da API", remoteError)

                    if (!isDebugBuild()) {
                        throw Exception("Não foi possível conectar ao servidor de autenticação.")
                    }

                    Log.d(TAG, "Ativando fallback local de debug para $normalizedEmail")

                    val fallbackUser = loginWithLocalFallback(normalizedEmail, normalizedPassword)
                    if (fallbackUser != null) {
                        Log.d(TAG, "Login via fallback local concluido para $normalizedEmail")
                        fallbackUser
                    } else {
                        throw IllegalArgumentException("Email ou senha incorretos.")
                    }
                }
            }
        }

    private fun loginWithLocalFallback(email: String, password: String): User? {
        if (password != TEST_PASSWORD) return null

        return when (email) {
            "operator@aguia.com" -> User(
                id = "op-001",
                name = "Operador Teste",
                email = email,
                role = UserRole.OPERATOR,
                unit = "Operação"
            )
            "manager@aguia.com" -> User(
                id = "mgr-001",
                name = "Gestor Teste",
                email = email,
                role = UserRole.MANAGER,
                unit = "Gestão"
            )
            "leadership@aguia.com" -> User(
                id = "lead-001",
                name = "Liderança Teste",
                email = email,
                role = UserRole.LEADERSHIP,
                unit = "Corporativo"
            )
            else -> null
        }
    }

    private fun isDebugBuild(): Boolean {
        return (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
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

