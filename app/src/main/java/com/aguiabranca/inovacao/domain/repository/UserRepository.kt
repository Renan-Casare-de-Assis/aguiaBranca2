package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getById(id: String): Result<User>
    suspend fun getAll(): Result<List<User>>
}

