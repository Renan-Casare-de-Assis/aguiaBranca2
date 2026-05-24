package com.aguiabranca.inovacao.domain.repository

import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus

interface ProjectRepository {
    suspend fun getAll(): Result<List<Project>>
    suspend fun getById(id: String): Result<Project>
    suspend fun create(
        ideaId: String?,
        title: String,
        objective: String,
        stage: ProjectStage,
        status: ProjectStatus,
        managerId: String,
        startDate: Long,
        targetEndDate: Long,
        investment: Double
    ): Result<Project>
    suspend fun update(
        id: String,
        title: String,
        objective: String,
        stage: ProjectStage,
        status: ProjectStatus,
        investment: Double,
        financialReturn: Double,
        costReduction: Double,
        productivityGainPct: Double,
        progressPct: Int
    ): Result<Project>
}
