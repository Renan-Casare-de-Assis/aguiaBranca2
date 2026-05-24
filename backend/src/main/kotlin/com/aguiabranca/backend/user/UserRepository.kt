package com.aguiabranca.backend.user

import com.aguiabranca.backend.util.safeString
import com.aguiabranca.backend.util.safeStringAny
import com.aguiabranca.backend.util.hasColumn
import com.aguiabranca.backend.util.timestampToMillis
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
open class UserRepository(private val jdbcTemplate: JdbcTemplate) {

    private val mapper = RowMapper { rs, _ ->
        UserResponse(
            id = rs.safeStringAny("ID", "RM", "MATRICULA", "USUARIO_ID", "CODIGO")
                ?: rs.safeString("EMAIL")
                ?: "",
            name = rs.safeStringAny("NOME", "NOME_COMPLETO") ?: "",
            email = rs.safeStringAny("EMAIL", "E_MAIL") ?: "",
            role = rs.safeStringAny("PERFIL", "CARGO", "PAPEL") ?: "",
            unit = rs.safeStringAny("UNIDADE", "FILIAL", "AREA"),
            avatarUrl = rs.safeString("AVATAR_URL"),
            createdAt = if (rs.hasColumn("CRIADO_EM")) rs.timestampToMillis("CRIADO_EM")
            else if (rs.hasColumn("DATA_CADASTRO")) rs.timestampToMillis("DATA_CADASTRO")
            else 0L
        )
    }

    open fun login(email: String, password: String): UserResponse? = jdbcTemplate.query(
        "SELECT * FROM USUARIOS WHERE EMAIL = ? AND SENHA = ?",
        mapper,
        email.trim().lowercase(),
        password.trim()
    ).firstOrNull()

    open fun findById(id: String): UserResponse? = jdbcTemplate.query(
        "SELECT * FROM USUARIOS WHERE ID = ?",
        mapper,
        id
    ).firstOrNull()

    open fun findAll(): List<UserResponse> = jdbcTemplate.query(
        "SELECT * FROM USUARIOS ORDER BY NOME",
        mapper
    )
}

