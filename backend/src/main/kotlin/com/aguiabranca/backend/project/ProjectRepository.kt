package com.aguiabranca.backend.project

import com.aguiabranca.backend.util.nullableTimestampToMillis
import com.aguiabranca.backend.util.safeDouble
import com.aguiabranca.backend.util.safeInt
import com.aguiabranca.backend.util.safeString
import com.aguiabranca.backend.util.timestampToMillis
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.util.UUID

@Component
open class ProjectRepository(private val jdbcTemplate: JdbcTemplate) {

    private val mapper = RowMapper { rs, _ ->
        ProjectResponse(
            id = rs.safeString("ID") ?: "",
            ideaId = rs.safeString("IDEIA_ID"),
            title = rs.safeString("TITULO") ?: "",
            objective = rs.safeString("OBJETIVO") ?: "",
            stage = rs.safeString("ETAPA") ?: "",
            status = rs.safeString("STATUS") ?: "",
            managerId = rs.safeString("GERENTE_ID") ?: "",
            startDate = rs.timestampToMillis("DATA_INICIO"),
            targetEndDate = rs.timestampToMillis("DATA_FIM_META"),
            actualEndDate = rs.nullableTimestampToMillis("DATA_FIM_REAL"),
            investment = rs.safeDouble("INVESTIMENTO"),
            financialReturn = rs.safeDouble("RETORNO_FINANCEIRO"),
            costReduction = rs.safeDouble("REDUCAO_CUSTO"),
            productivityGainPct = rs.safeDouble("GANHO_PRODUTIVIDADE_PCT"),
            profit = rs.safeDouble("LUCRO"),
            roi = rs.safeDouble("ROI"),
            progressPct = rs.safeInt("PROGRESSO_PCT"),
            createdAt = rs.timestampToMillis("CRIADO_EM"),
            updatedAt = rs.timestampToMillis("ATUALIZADO_EM")
        )
    }

    open fun findAll(): List<ProjectResponse> = jdbcTemplate.query(
        "SELECT * FROM PROJETOS ORDER BY CRIADO_EM DESC",
        mapper
    )

    open fun findById(id: String): ProjectResponse? = jdbcTemplate.query(
        "SELECT * FROM PROJETOS WHERE ID = ?",
        mapper,
        id
    ).firstOrNull()

    open fun create(request: CreateProjectRequest): ProjectResponse {
        val id = UUID.randomUUID().toString()
        jdbcTemplate.update(
            """
            INSERT INTO PROJETOS (
                ID, IDEIA_ID, TITULO, OBJETIVO, ETAPA, STATUS, GERENTE_ID,
                DATA_INICIO, DATA_FIM_META, INVESTIMENTO, CRIADO_EM, ATUALIZADO_EM
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)
            """.trimIndent(),
            id,
            request.ideaId,
            request.title,
            request.objective,
            request.stage,
            request.status,
            request.managerId,
            Timestamp(request.startDate),
            Timestamp(request.targetEndDate),
            request.investment
        )
        return findById(id) ?: throw IllegalStateException("Projeto não encontrado após criação")
    }

    open fun update(id: String, request: UpdateProjectRequest): ProjectResponse {
        val profit = request.financialReturn - request.investment
        val roi = if (request.investment > 0) ((request.financialReturn - request.investment) / request.investment) * 100 else 0.0
        val updated = jdbcTemplate.update(
            """
            UPDATE PROJETOS
            SET TITULO = ?, OBJETIVO = ?, ETAPA = ?, STATUS = ?,
                INVESTIMENTO = ?, RETORNO_FINANCEIRO = ?, REDUCAO_CUSTO = ?,
                GANHO_PRODUTIVIDADE_PCT = ?, LUCRO = ?, ROI = ?, PROGRESSO_PCT = ?,
                ATUALIZADO_EM = SYSTIMESTAMP
            WHERE ID = ?
            """.trimIndent(),
            request.title,
            request.objective,
            request.stage,
            request.status,
            request.investment,
            request.financialReturn,
            request.costReduction,
            request.productivityGainPct,
            profit,
            roi,
            request.progressPct,
            id
        )
        if (updated == 0) throw NoSuchElementException("Projeto não encontrado")
        return findById(id) ?: throw IllegalStateException("Projeto não encontrado após atualização")
    }

    open fun delete(id: String) {
        val deleted = jdbcTemplate.update("DELETE FROM PROJETOS WHERE ID = ?", id)
        if (deleted == 0) throw NoSuchElementException("Projeto não encontrado")
    }
}

