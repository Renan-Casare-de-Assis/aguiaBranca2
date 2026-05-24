package com.aguiabranca.inovacao.data.repository

import android.util.Log
import android.content.Context
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.remote.oracle.OracleDataSource
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
    @ApplicationContext private val context: Context
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
        private const val TEST_PASSWORD = "070589"
    }

    private fun mapUser(rs: java.sql.ResultSet): User = User(
        id = rs.getString("ID"),
        name = rs.getString("NOME"),
        email = rs.getString("EMAIL"),
        role = UserRole.fromDb(rs.getString("PERFIL")),
        unit = rs.getString("UNIDADE"),
        avatarUrl = rs.getString("AVATAR_URL"),
        createdAt = rs.getTimestamp("CRIADO_EM")?.time ?: 0L
    )

    override suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                val normalizedEmail = email.trim().lowercase()
                val normalizedPassword = password.trim()

                try {
                    Log.d(TAG, "Tentando login Oracle para $normalizedEmail")
                    OracleDataSource.execute { conn ->
                        val sql = "SELECT * FROM USUARIOS WHERE EMAIL = ? AND SENHA = ?"
                        val stmt = conn.prepareStatement(sql)
                        stmt.setString(1, normalizedEmail)
                        stmt.setString(2, normalizedPassword)
                        val rs = stmt.executeQuery()
                        if (!rs.next()) throw IllegalArgumentException("Email ou senha incorretos.")
                        mapUser(rs)
                    }.getOrThrow()
                } catch (remoteError: Throwable) {
                    Log.e(TAG, "Falha no login remoto Oracle", remoteError)

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
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM USUARIOS WHERE ID = ?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, id)
                    val rs = stmt.executeQuery()
                    if (!rs.next()) throw Exception("Usuário não encontrado")
                    mapUser(rs)
                }.getOrThrow()
            }
        }

    override suspend fun getAll(): Result<List<User>> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM USUARIOS ORDER BY NOME"
                    val stmt = conn.prepareStatement(sql)
                    val rs = stmt.executeQuery()
                    val list = mutableListOf<User>()
                    while (rs.next()) {
                        list.add(mapUser(rs))
                    }
                    list
                }.getOrThrow()
            }
        }
}

