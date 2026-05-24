package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.Guideline

interface GuidelineRepository {
    suspend fun getAll(): Result<List<Guideline>>
    suspend fun getById(id: String): Result<Guideline>
    suspend fun create(title: String, description: String, category: String, authorId: String): Result<Guideline>
    suspend fun update(id: String, title: String, description: String, category: String): Result<Guideline>
    suspend fun delete(id: String): Result<Unit>
}
