package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.local.room.dao.IdeaDao
import com.aguiabranca.inovacao.data.local.room.entity.IdeaEntity
import com.aguiabranca.inovacao.data.remote.oracle.OracleDataSource
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaCategory
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaRepositoryImpl @Inject constructor(
    private val ideaDao: IdeaDao
) : IdeaRepository {

    private fun mapRow(rs: java.sql.ResultSet): Idea = Idea(
        id            = rs.getString("ID"),
        title         = rs.getString("TITULO"),
        description   = rs.getString("DESCRICAO"),
        category      = IdeaCategory.fromDb(rs.getString("CATEGORIA")),
        operatorId    = rs.getString("OPERADOR_ID") ?: "",
        operatorName  = rs.getString("OPERADOR_NOME"),
        unit          = rs.getString("UNIDADE"),
        status        = IdeaStatus.fromDb(rs.getString("STATUS")),
        priority      = IdeaPriority.fromDb(rs.getString("PRIORIDADE")),
        managerComment= rs.getString("COMENTARIO_GERENTE"),
        approvedBy    = rs.getString("APROVADA_POR"),
        approvedAt    = rs.getTimestamp("APROVADA_EM")?.time,
        createdAt     = rs.getTimestamp("CRIADO_EM")?.time ?: 0L,
        updatedAt     = rs.getTimestamp("ATUALIZADO_EM")?.time ?: 0L
    )

    override suspend fun getMyIdeas(operatorId: String): Result<List<Idea>> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM IDEIAS WHERE OPERADOR_ID = ? ORDER BY CRIADO_EM DESC"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, operatorId)
                    val rs = stmt.executeQuery()
                    val list = mutableListOf<Idea>()
                    while (rs.next()) list.add(mapRow(rs))
                    list
                }.getOrThrow()
            }.recoverCatching {
                ideaDao.getByOperator(operatorId).map { e ->
                    Idea(e.id, e.title, e.description,
                        IdeaCategory.fromDb(e.category), e.operatorId,
                        e.operatorName, e.unit, IdeaStatus.fromDb(e.status),
                        IdeaPriority.fromDb(e.priority), e.managerComment,
                        e.approvedBy, e.approvedAt, e.createdAt, e.updatedAt)
                }
            }
        }

    override suspend fun getAllIdeas(): Result<List<Idea>> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM IDEIAS ORDER BY CRIADO_EM DESC"
                    val stmt = conn.prepareStatement(sql)
                    val rs = stmt.executeQuery()
                    val list = mutableListOf<Idea>()
                    while (rs.next()) list.add(mapRow(rs))
                    list
                }.getOrThrow()
            }.recoverCatching {
                ideaDao.getAll().map { e ->
                    Idea(e.id, e.title, e.description,
                        IdeaCategory.fromDb(e.category), e.operatorId,
                        e.operatorName, e.unit, IdeaStatus.fromDb(e.status),
                        IdeaPriority.fromDb(e.priority), e.managerComment,
                        e.approvedBy, e.approvedAt, e.createdAt, e.updatedAt)
                }
            }
        }

    override suspend fun create(title: String, description: String, category: IdeaCategory, operatorId: String, operatorName: String, unit: String?): Result<Idea> =
        withContext(Dispatchers.IO) {
            runCatching {
                val id = UUID.randomUUID().toString()
                OracleDataSource.execute { conn ->
                    val sql = "INSERT INTO IDEIAS (ID, TITULO, DESCRICAO, CATEGORIA, OPERADOR_ID, OPERADOR_NOME, UNIDADE, STATUS, CRIADO_EM, ATUALIZADO_EM) VALUES (?, ?, ?, ?, ?, ?, ?, 'NOVA', SYSTIMESTAMP, SYSTIMESTAMP)"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, id)
                    stmt.setString(2, title)
                    stmt.setString(3, description)
                    stmt.setString(4, category.toDb())
                    stmt.setString(5, operatorId)
                    stmt.setString(6, operatorName)
                    stmt.setString(7, unit)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
                val idea = Idea(id, title, description, category, operatorId,
                    operatorName, unit, IdeaStatus.NEW, null, null,
                    null, null, System.currentTimeMillis(), System.currentTimeMillis())
                ideaDao.insert(IdeaEntity(id, title, description, category.toDb(),
                    operatorId, operatorName, unit, IdeaStatus.NEW.toDb(),
                    null, null, null, null,
                    System.currentTimeMillis(), System.currentTimeMillis()))
                idea
            }
        }

    override suspend fun updateStatus(id: String, status: IdeaStatus, comment: String?): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "UPDATE IDEIAS SET STATUS=?, COMENTARIO_GERENTE=?, ATUALIZADO_EM=SYSTIMESTAMP WHERE ID=?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, status.toDb())
                    stmt.setString(2, comment)
                    stmt.setString(3, id)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
            }
        }

    override suspend fun updatePriority(id: String, priority: IdeaPriority): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "UPDATE IDEIAS SET PRIORIDADE=?, STATUS='PRIORIZADA', ATUALIZADO_EM=SYSTIMESTAMP WHERE ID=?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, priority.toDb())
                    stmt.setString(2, id)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
            }
        }
}

