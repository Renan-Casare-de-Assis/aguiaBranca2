package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.GuidelineStatus

interface GuidelineRepository {
    suspend fun getAll(): Result<List<Guideline>>
    suspend fun create(title: String, description: String, pillar: String, createdBy: String): Result<Guideline>
    suspend fun update(id: String, title: String, description: String, pillar: String, status: GuidelineStatus): Result<Guideline>
    suspend fun delete(id: String): Result<Unit>
}

