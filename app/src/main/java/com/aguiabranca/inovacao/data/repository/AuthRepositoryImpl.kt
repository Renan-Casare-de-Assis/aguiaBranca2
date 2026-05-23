package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.core.session.SessionManager
import com.aguiabranca.inovacao.data.local.room.dao.UserDao
import com.aguiabranca.inovacao.data.local.room.entity.UserEntity
import com.aguiabranca.inovacao.data.remote.oracle.OracleDataSource
import com.aguiabranca.inovacao.domain.model.Session
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Session> =
        withContext(Dispatchers.IO) {
            runCatching {
                // Busca usuário no Oracle pelo email
                val result = OracleDataSource.execute { conn ->
                    val sql = "SELECT ID_USUARIO, NOME, EMAIL, PAPEL, AREA, ATIVO FROM USUARIOS WHERE EMAIL = ? AND ATIVO = 1"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, email)
                    val rs = stmt.executeQuery()
                    if (!rs.next()) throw Exception("Email ou senha incorretos")
                    User(
                        uid    = rs.getString("ID_USUARIO"),
                        name   = rs.getString("NOME"),
                        email  = rs.getString("EMAIL"),
                        role   = UserRole.fromDb(rs.getString("PAPEL")),
                        area   = rs.getString("AREA"),
                        active = rs.getInt("ATIVO") == 1
                    )
                }
                val user = result.getOrThrow()
                // Cache local
                userDao.insert(UserEntity(user.uid, user.name, user.email, user.role.toDb(), user.area, user.active))
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

