package com.aguiabranca.backend.idea

import com.aguiabranca.backend.util.nullableTimestampToMillis
import com.aguiabranca.backend.util.timestampToMillis
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.util.UUID

@Component
open class IdeaRepository(private val jdbcTemplate: JdbcTemplate) {

    private val mapper = RowMapper { rs, _ ->
        IdeaResponse(
            id = rs.getString("ID"),
            title = rs.getString("TITULO"),
            description = rs.getString("DESCRICAO"),
            category = rs.getString("CATEGORIA"),
            operatorId = rs.getString("OPERADOR_ID"),
            operatorName = rs.getString("OPERADOR_NOME"),
            unit = rs.getString("UNIDADE"),
            status = rs.getString("STATUS"),
            priority = rs.getString("PRIORIDADE"),
            managerComment = rs.getString("COMENTARIO_GERENTE"),
            approvedBy = rs.getString("APROVADA_POR"),
            approvedAt = rs.nullableTimestampToMillis("APROVADA_EM"),
            createdAt = rs.timestampToMillis("CRIADO_EM"),
            updatedAt = rs.timestampToMillis("ATUALIZADO_EM")
        )
    }

    open fun findAll(): List<IdeaResponse> = jdbcTemplate.query(
        "SELECT * FROM IDEIAS ORDER BY CRIADO_EM DESC",
        mapper
    )

    open fun findByOperator(operatorId: String): List<IdeaResponse> = jdbcTemplate.query(
        "SELECT * FROM IDEIAS WHERE OPERADOR_ID = ? ORDER BY CRIADO_EM DESC",
        mapper,
        operatorId
    )

    open fun findById(id: String): IdeaResponse? = jdbcTemplate.query(
        "SELECT * FROM IDEIAS WHERE ID = ?",
        mapper,
        id
    ).firstOrNull()

    open fun create(request: CreateIdeaRequest): IdeaResponse {
        val id = UUID.randomUUID().toString()
        jdbcTemplate.update(
            """
            INSERT INTO IDEIAS (
                ID, TITULO, DESCRICAO, CATEGORIA, OPERADOR_ID,
                OPERADOR_NOME, UNIDADE, STATUS, CRIADO_EM, ATUALIZADO_EM
            ) VALUES (?, ?, ?, ?, ?, ?, ?, 'NOVA', SYSTIMESTAMP, SYSTIMESTAMP)
            """.trimIndent(),
            id,
            request.title,
            request.description,
            request.category,
            request.operatorId,
            request.operatorName,
            request.unit
        )
        return findById(id) ?: throw IllegalStateException("Ideia não encontrada após criação")
    }

    open fun updateStatus(id: String, request: UpdateIdeaStatusRequest): IdeaResponse {
        val updated = jdbcTemplate.update(
            "UPDATE IDEIAS SET STATUS = ?, COMENTARIO_GERENTE = ?, ATUALIZADO_EM = SYSTIMESTAMP WHERE ID = ?",
            request.status,
            request.comment,
            id
        )
        if (updated == 0) throw NoSuchElementException("Ideia não encontrada")
        return findById(id) ?: throw IllegalStateException("Ideia não encontrada após atualização")
    }

    open fun updatePriority(id: String, request: UpdateIdeaPriorityRequest): IdeaResponse {
        val updated = jdbcTemplate.update(
            "UPDATE IDEIAS SET PRIORIDADE = ?, STATUS = 'PRIORIZADA', ATUALIZADO_EM = SYSTIMESTAMP WHERE ID = ?",
            request.priority,
            id
        )
        if (updated == 0) throw NoSuchElementException("Ideia não encontrada")
        return findById(id) ?: throw IllegalStateException("Ideia não encontrada após atualização")
    }

    open fun delete(id: String) {
        val deleted = jdbcTemplate.update("DELETE FROM IDEIAS WHERE ID = ?", id)
        if (deleted == 0) throw NoSuchElementException("Ideia não encontrada")
    }
}

