package com.aguiabranca.backend.guideline

import com.aguiabranca.backend.util.nullableTimestampToMillis
import com.aguiabranca.backend.util.timestampToMillis
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.util.UUID

@Component
open class GuidelineRepository(private val jdbcTemplate: JdbcTemplate) {

    private val mapper = RowMapper { rs, _ ->
        GuidelineResponse(
            id = rs.getString("ID"),
            title = rs.getString("TITULO"),
            description = rs.getString("DESCRICAO"),
            pillar = rs.getString("PILAR"),
            validFrom = rs.nullableTimestampToMillis("VALIDO_DE"),
            validTo = rs.nullableTimestampToMillis("VALIDO_ATE"),
            status = rs.getString("STATUS"),
            createdBy = rs.getString("CRIADO_POR"),
            createdAt = rs.timestampToMillis("CRIADO_EM"),
            updatedAt = rs.timestampToMillis("ATUALIZADO_EM")
        )
    }

    open fun findAll(): List<GuidelineResponse> = jdbcTemplate.query(
        "SELECT ID, TITULO, DESCRICAO, PILAR, VALIDO_DE, VALIDO_ATE, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM FROM DIRETRIZES ORDER BY CRIADO_EM DESC",
        mapper
    )

    open fun findById(id: String): GuidelineResponse? = jdbcTemplate.query(
        "SELECT ID, TITULO, DESCRICAO, PILAR, VALIDO_DE, VALIDO_ATE, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM FROM DIRETRIZES WHERE ID = ?",
        mapper,
        id
    ).firstOrNull()

    open fun create(request: CreateGuidelineRequest): GuidelineResponse {
        val id = UUID.randomUUID().toString()
        jdbcTemplate.update(
            "INSERT INTO DIRETRIZES (ID, TITULO, DESCRICAO, PILAR, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM) VALUES (?, ?, ?, ?, 'ATIVO', ?, SYSTIMESTAMP, SYSTIMESTAMP)",
            id,
            request.title,
            request.description,
            request.category,
            request.authorId
        )
        return findById(id) ?: throw IllegalStateException("Diretriz não encontrada após criação")
    }

    open fun update(id: String, request: UpdateGuidelineRequest): GuidelineResponse {
        val updated = jdbcTemplate.update(
            "UPDATE DIRETRIZES SET TITULO = ?, DESCRICAO = ?, PILAR = ?, ATUALIZADO_EM = SYSTIMESTAMP WHERE ID = ?",
            request.title,
            request.description,
            request.category,
            id
        )
        if (updated == 0) throw NoSuchElementException("Diretriz não encontrada")
        return findById(id) ?: throw IllegalStateException("Diretriz não encontrada após atualização")
    }

    open fun delete(id: String) {
        val deleted = jdbcTemplate.update("DELETE FROM DIRETRIZES WHERE ID = ?", id)
        if (deleted == 0) throw NoSuchElementException("Diretriz não encontrada")
    }
}

