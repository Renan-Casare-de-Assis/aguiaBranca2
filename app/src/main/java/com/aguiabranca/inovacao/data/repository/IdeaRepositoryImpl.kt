package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.local.room.dao.IdeaDao
import com.aguiabranca.inovacao.data.local.room.entity.IdeaEntity
import com.aguiabranca.inovacao.data.remote.api.CreateIdeaRequestDto
import com.aguiabranca.inovacao.data.remote.api.IdeaApiService
import com.aguiabranca.inovacao.data.remote.api.UpdateIdeaPriorityRequestDto
import com.aguiabranca.inovacao.data.remote.api.UpdateIdeaStatusRequestDto
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaCategory
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdeaRepositoryImpl @Inject constructor(
    private val ideaDao: IdeaDao,
    private val ideaApi: IdeaApiService
) : IdeaRepository {

    private fun Idea.toEntity() = IdeaEntity(
        id = id,
        title = title,
        description = description,
        category = category.toDb(),
        operatorId = operatorId,
        operatorName = operatorName,
        unit = unit,
        status = status.toDb(),
        priority = priority?.toDb(),
        managerComment = managerComment,
        approvedBy = approvedBy,
        approvedAt = approvedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun IdeaEntity.toDomain() = Idea(
        id,
        title,
        description,
        IdeaCategory.fromDb(category),
        operatorId,
        operatorName,
        unit,
        IdeaStatus.fromDb(status),
        IdeaPriority.fromDb(priority),
        managerComment,
        approvedBy,
        approvedAt,
        createdAt,
        updatedAt
    )

    override suspend fun getByOperator(operatorId: String): Result<List<Idea>> = withContext(Dispatchers.IO) {
        runCatching {
            val remote = ideaApi.getAll(operatorId).map { it.toDomain() }
            ideaDao.deleteByOperator(operatorId)
            ideaDao.insertAll(remote.map { it.toEntity() })
            remote
        }.recoverCatching {
            ideaDao.getByOperator(operatorId).map { it.toDomain() }
        }
    }

    override suspend fun getAll(): Result<List<Idea>> = withContext(Dispatchers.IO) {
        runCatching {
            val remote = ideaApi.getAll().map { it.toDomain() }
            ideaDao.deleteAll()
            ideaDao.insertAll(remote.map { it.toEntity() })
            remote
        }.recoverCatching {
            ideaDao.getAll().map { it.toDomain() }
        }
    }

    override suspend fun getById(id: String): Result<Idea> = withContext(Dispatchers.IO) {
        runCatching {
            val remote = ideaApi.getById(id).toDomain()
            ideaDao.insert(remote.toEntity())
            remote
        }.recoverCatching {
            ideaDao.getById(id)?.toDomain() ?: throw Exception("Ideia não encontrada")
        }
    }

    override suspend fun create(
        title: String,
        description: String,
        category: IdeaCategory,
        operatorId: String,
        operatorName: String,
        unit: String?
    ): Result<Idea> = withContext(Dispatchers.IO) {
        runCatching {
            val created = ideaApi.create(
                CreateIdeaRequestDto(
                    title = title,
                    description = description,
                    category = category.toDb(),
                    operatorId = operatorId,
                    operatorName = operatorName,
                    unit = unit
                )
            ).toDomain()
            ideaDao.insert(created.toEntity())
            created
        }
    }

    override suspend fun updateStatus(id: String, status: IdeaStatus, comment: String?): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val updated = ideaApi.updateStatus(id, UpdateIdeaStatusRequestDto(status = status.toDb(), comment = comment)).toDomain()
            ideaDao.update(updated.toEntity())
        }
    }

    override suspend fun updatePriority(id: String, priority: IdeaPriority): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val updated = ideaApi.updatePriority(id, UpdateIdeaPriorityRequestDto(priority = priority.toDb())).toDomain()
            ideaDao.update(updated.toEntity())
        }
    }

    override suspend fun delete(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            ideaApi.delete(id)
            ideaDao.deleteById(id)
        }
    }
}
