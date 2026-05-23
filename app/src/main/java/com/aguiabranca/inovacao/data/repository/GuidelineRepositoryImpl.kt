package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.local.room.dao.GuidelineDao
import com.aguiabranca.inovacao.data.local.room.entity.GuidelineEntity
import com.aguiabranca.inovacao.data.remote.oracle.OracleDataSource
import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.GuidelineStatus
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuidelineRepositoryImpl @Inject constructor(
    private val guidelineDao: GuidelineDao
) : GuidelineRepository {

    override suspend fun getAll(): Result<List<Guideline>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = OracleDataSource.execute { conn ->
                    val sql = "SELECT ID, TITULO, DESCRICAO, PILAR, VALIDO_DE, VALIDO_ATE, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM FROM DIRETRIZES ORDER BY CRIADO_EM DESC"
                    val stmt = conn.prepareStatement(sql)
                    val rs = stmt.executeQuery()
                    val list = mutableListOf<Guideline>()
                    while (rs.next()) {
                        list.add(
                            Guideline(
                                id          = rs.getString("ID"),
                                title       = rs.getString("TITULO"),
                                description = rs.getString("DESCRICAO"),
                                pillar      = rs.getString("PILAR"),
                                validFrom   = rs.getTimestamp("VALIDO_DE")?.time,
                                validTo     = rs.getTimestamp("VALIDO_ATE")?.time,
                                status      = GuidelineStatus.fromDb(rs.getString("STATUS")),
                                createdBy   = rs.getString("CRIADO_POR") ?: "",
                                createdAt   = rs.getTimestamp("CRIADO_EM")?.time ?: 0L,
                                updatedAt   = rs.getTimestamp("ATUALIZADO_EM")?.time ?: 0L
                            )
                        )
                    }
                    list
                }
                val guidelines = result.getOrThrow()
                // Cache local
                guidelineDao.deleteAll()
                guidelineDao.insertAll(guidelines.map {
                    GuidelineEntity(it.id, it.title, it.description, it.pillar,
                        it.validFrom, it.validTo, it.status.toDb(), it.createdBy,
                        it.createdAt, it.updatedAt)
                })
                guidelines
            }.recoverCatching {
                // Fallback para cache local se Oracle falhar
                guidelineDao.getAll().map { e ->
                    Guideline(e.id, e.title, e.description, e.pillar,
                        e.validFrom, e.validTo, GuidelineStatus.fromDb(e.status),
                        e.createdBy, e.createdAt, e.updatedAt)
                }
            }
        }

    override suspend fun create(title: String, description: String, pillar: String, createdBy: String): Result<Guideline> =
        withContext(Dispatchers.IO) {
            runCatching {
                val id = UUID.randomUUID().toString()
                OracleDataSource.execute { conn ->
                    val sql = "INSERT INTO DIRETRIZES (ID, TITULO, DESCRICAO, PILAR, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM) VALUES (?, ?, ?, ?, 'ATIVO', ?, SYSTIMESTAMP, SYSTIMESTAMP)"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, id)
                    stmt.setString(2, title)
                    stmt.setString(3, description)
                    stmt.setString(4, pillar)
                    stmt.setString(5, createdBy)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
                Guideline(id, title, description, pillar, null, null,
                    GuidelineStatus.ACTIVE, createdBy,
                    System.currentTimeMillis(), System.currentTimeMillis())
            }
        }

    override suspend fun update(id: String, title: String, description: String, pillar: String, status: GuidelineStatus): Result<Guideline> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "UPDATE DIRETRIZES SET TITULO=?, DESCRICAO=?, PILAR=?, STATUS=?, ATUALIZADO_EM=SYSTIMESTAMP WHERE ID=?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, title)
                    stmt.setString(2, description)
                    stmt.setString(3, pillar)
                    stmt.setString(4, status.toDb())
                    stmt.setString(5, id)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
                Guideline(id, title, description, pillar, null, null,
                    status, "", System.currentTimeMillis(), System.currentTimeMillis())
            }
        }

    override suspend fun delete(id: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "DELETE FROM DIRETRIZES WHERE ID = ?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, id)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
                guidelineDao.deleteById(id)
            }
        }
}

