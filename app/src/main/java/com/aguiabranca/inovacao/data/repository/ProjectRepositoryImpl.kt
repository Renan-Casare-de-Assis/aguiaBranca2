package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.local.room.dao.ProjectDao
import com.aguiabranca.inovacao.data.local.room.entity.ProjectEntity
import com.aguiabranca.inovacao.data.remote.api.CreateProjectRequestDto
import com.aguiabranca.inovacao.data.remote.api.ProjectApiService
import com.aguiabranca.inovacao.data.remote.api.UpdateProjectRequestDto
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val projectApi: ProjectApiService
) : ProjectRepository {

    private fun Project.toEntity() = ProjectEntity(
        id = id,
        ideaId = ideaId,
        title = title,
        objective = objective,
        stage = stage.toDb(),
        status = status.toDb(),
        managerId = managerId,
        startDate = startDate,
        targetEndDate = targetEndDate,
        actualEndDate = actualEndDate,
        investment = investment,
        financialReturn = financialReturn,
        costReduction = costReduction,
        productivityGainPct = productivityGainPct,
        profit = profit,
        roi = roi,
        progressPct = progressPct,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun ProjectEntity.toDomain() = Project(
        id = id,
        ideaId = ideaId,
        title = title,
        objective = objective,
        stage = ProjectStage.fromDb(stage),
        status = ProjectStatus.fromDb(status),
        managerId = managerId,
        startDate = startDate,
        targetEndDate = targetEndDate,
        actualEndDate = actualEndDate,
        investment = investment,
        financialReturn = financialReturn,
        costReduction = costReduction,
        productivityGainPct = productivityGainPct,
        profit = profit,
        roi = roi,
        progressPct = progressPct,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    override suspend fun getAll(): Result<List<Project>> = withContext(Dispatchers.IO) {
        runCatching {
            val remote = projectApi.getAll().map { it.toDomain() }
            projectDao.deleteAll()
            projectDao.insertAll(remote.map { it.toEntity() })
            remote
        }.recoverCatching {
            projectDao.getAll().map { it.toDomain() }
        }
    }

    override suspend fun getById(id: String): Result<Project> = withContext(Dispatchers.IO) {
        runCatching {
            val remote = projectApi.getById(id).toDomain()
            projectDao.insert(remote.toEntity())
            remote
        }.recoverCatching {
            projectDao.getById(id)?.toDomain() ?: throw Exception("Projeto não encontrado")
        }
    }

    override suspend fun create(
        ideaId: String?,
        title: String,
        objective: String,
        stage: ProjectStage,
        status: ProjectStatus,
        managerId: String,
        startDate: Long,
        targetEndDate: Long,
        investment: Double
    ): Result<Project> = withContext(Dispatchers.IO) {
        runCatching {
            val created = projectApi.create(
                CreateProjectRequestDto(
                    ideaId = ideaId,
                    title = title,
                    objective = objective,
                    stage = stage.toDb(),
                    status = status.toDb(),
                    managerId = managerId,
                    startDate = startDate,
                    targetEndDate = targetEndDate,
                    investment = investment
                )
            ).toDomain()
            projectDao.insert(created.toEntity())
            created
        }
    }

    override suspend fun update(
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
    ): Result<Project> = withContext(Dispatchers.IO) {
        runCatching {
            val updated = projectApi.update(
                id,
                UpdateProjectRequestDto(
                    title = title,
                    objective = objective,
                    stage = stage.toDb(),
                    status = status.toDb(),
                    investment = investment,
                    financialReturn = financialReturn,
                    costReduction = costReduction,
                    productivityGainPct = productivityGainPct,
                    progressPct = progressPct
                )
            ).toDomain()
            projectDao.update(updated.toEntity())
            updated
        }
    }

    override suspend fun delete(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            projectApi.delete(id)
            projectDao.deleteById(id)
        }
    }
}
