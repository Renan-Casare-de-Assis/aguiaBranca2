package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.local.room.dao.ProjectDao
import com.aguiabranca.inovacao.data.local.room.entity.ProjectEntity
import com.aguiabranca.inovacao.data.remote.oracle.OracleDataSource
import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao
) : ProjectRepository {

    private fun mapRow(rs: java.sql.ResultSet): Project = Project(
        id                  = rs.getString("ID"),
        ideaId              = rs.getString("IDEIA_ID"),
        title               = rs.getString("TITULO"),
        objective           = rs.getString("OBJETIVO"),
        stage               = ProjectStage.fromDb(rs.getString("ETAPA")),
        status              = ProjectStatus.fromDb(rs.getString("STATUS")),
        managerId           = rs.getString("GERENTE_ID") ?: "",
        startDate           = rs.getTimestamp("DATA_INICIO")?.time ?: 0L,
        targetEndDate       = rs.getTimestamp("DATA_FIM_META")?.time ?: 0L,
        actualEndDate       = rs.getTimestamp("DATA_FIM_REAL")?.time,
        investment          = rs.getDouble("INVESTIMENTO"),
        financialReturn     = rs.getDouble("RETORNO_FINANCEIRO"),
        costReduction       = rs.getDouble("REDUCAO_CUSTO"),
        productivityGainPct = rs.getDouble("GANHO_PRODUTIVIDADE_PCT"),
        profit              = rs.getDouble("LUCRO"),
        roi                 = rs.getDouble("ROI"),
        progressPct         = rs.getInt("PROGRESSO_PCT"),
        createdAt           = rs.getTimestamp("CRIADO_EM")?.time ?: 0L,
        updatedAt           = rs.getTimestamp("ATUALIZADO_EM")?.time ?: 0L
    )

    override suspend fun getAll(): Result<List<Project>> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM PROJETOS ORDER BY CRIADO_EM DESC"
                    val stmt = conn.prepareStatement(sql)
                    val rs = stmt.executeQuery()
                    val list = mutableListOf<Project>()
                    while (rs.next()) list.add(mapRow(rs))
                    list
                }.getOrThrow()
            }.recoverCatching {
                projectDao.getAll().map { e ->
                    Project(e.id, e.ideaId, e.title, e.objective,
                        ProjectStage.fromDb(e.stage), ProjectStatus.fromDb(e.status),
                        e.managerId, e.startDate, e.targetEndDate, e.actualEndDate,
                        e.investment, e.financialReturn, e.costReduction,
                        e.productivityGainPct, e.profit, e.roi, e.progressPct,
                        e.createdAt, e.updatedAt)
                }
            }
        }

    override suspend fun getById(id: String): Result<Project> =
        withContext(Dispatchers.IO) {
            runCatching {
                OracleDataSource.execute { conn ->
                    val sql = "SELECT * FROM PROJETOS WHERE ID = ?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, id)
                    val rs = stmt.executeQuery()
                    if (!rs.next()) throw Exception("Projeto não encontrado")
                    mapRow(rs)
                }.getOrThrow()
            }
        }

    override suspend fun create(ideaId: String?, title: String, objective: String, stage: ProjectStage, status: ProjectStatus, managerId: String, startDate: Long, targetEndDate: Long, investment: Double): Result<Project> =
        withContext(Dispatchers.IO) {
            runCatching {
                val id = UUID.randomUUID().toString()
                OracleDataSource.execute { conn ->
                    val sql = "INSERT INTO PROJETOS (ID, IDEIA_ID, TITULO, OBJETIVO, ETAPA, STATUS, GERENTE_ID, DATA_INICIO, DATA_FIM_META, INVESTIMENTO, CRIADO_EM, ATUALIZADO_EM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, id)
                    stmt.setString(2, ideaId)
                    stmt.setString(3, title)
                    stmt.setString(4, objective)
                    stmt.setString(5, stage.toDb())
                    stmt.setString(6, status.toDb())
                    stmt.setString(7, managerId)
                    stmt.setTimestamp(8, Timestamp(startDate))
                    stmt.setTimestamp(9, Timestamp(targetEndDate))
                    stmt.setDouble(10, investment)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
                Project(id, ideaId, title, objective, stage, status, managerId,
                    startDate, targetEndDate, null, investment, 0.0, 0.0,
                    0.0, 0.0, 0.0, 0, System.currentTimeMillis(), System.currentTimeMillis())
            }
        }

    override suspend fun update(id: String, title: String, objective: String, stage: ProjectStage, status: ProjectStatus, investment: Double, financialReturn: Double, costReduction: Double, productivityGainPct: Double, progressPct: Int): Result<Project> =
        withContext(Dispatchers.IO) {
            runCatching {
                val profit = financialReturn - investment
                val roi = if (investment > 0) ((financialReturn - investment) / investment) * 100 else 0.0
                OracleDataSource.execute { conn ->
                    val sql = "UPDATE PROJETOS SET TITULO=?, OBJETIVO=?, ETAPA=?, STATUS=?, INVESTIMENTO=?, RETORNO_FINANCEIRO=?, REDUCAO_CUSTO=?, GANHO_PRODUTIVIDADE_PCT=?, LUCRO=?, ROI=?, PROGRESSO_PCT=?, ATUALIZADO_EM=SYSTIMESTAMP WHERE ID=?"
                    val stmt = conn.prepareStatement(sql)
                    stmt.setString(1, title)
                    stmt.setString(2, objective)
                    stmt.setString(3, stage.toDb())
                    stmt.setString(4, status.toDb())
                    stmt.setDouble(5, investment)
                    stmt.setDouble(6, financialReturn)
                    stmt.setDouble(7, costReduction)
                    stmt.setDouble(8, productivityGainPct)
                    stmt.setDouble(9, profit)
                    stmt.setDouble(10, roi)
                    stmt.setInt(11, progressPct)
                    stmt.setString(12, id)
                    stmt.executeUpdate()
                    conn.commit()
                }.getOrThrow()
                getById(id).getOrThrow()
            }
        }
}

