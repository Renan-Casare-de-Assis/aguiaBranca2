package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaCategory
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus

interface IdeaRepository {
    suspend fun getAll(): Result<List<Idea>>
    suspend fun getByOperator(operatorId: String): Result<List<Idea>>
    suspend fun getById(id: String): Result<Idea>
    suspend fun create(title: String, description: String, category: IdeaCategory, operatorId: String, operatorName: String, unit: String?): Result<Idea>
    suspend fun updateStatus(id: String, status: IdeaStatus, comment: String?): Result<Unit>
    suspend fun updatePriority(id: String, priority: IdeaPriority): Result<Unit>
}
