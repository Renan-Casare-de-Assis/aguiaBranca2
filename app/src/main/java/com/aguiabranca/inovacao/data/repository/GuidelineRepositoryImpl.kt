package com.aguiabranca.inovacao.data.repository

import com.aguiabranca.inovacao.data.local.room.dao.GuidelineDao
import com.aguiabranca.inovacao.data.local.room.entity.GuidelineEntity
import com.aguiabranca.inovacao.data.remote.api.CreateGuidelineRequestDto
import com.aguiabranca.inovacao.data.remote.api.GuidelineApiService
import com.aguiabranca.inovacao.data.remote.api.UpdateGuidelineRequestDto
import com.aguiabranca.inovacao.data.remote.api.toDomain
import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.GuidelineStatus
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuidelineRepositoryImpl @Inject constructor(
    private val guidelineDao: GuidelineDao,
    private val guidelineApi: GuidelineApiService
) : GuidelineRepository {

    override suspend fun getById(id: String): Result<Guideline> =
        withContext(Dispatchers.IO) {
            runCatching {
                val remote = guidelineApi.getById(id).toDomain()
                guidelineDao.insert(
                    GuidelineEntity(
                        remote.id,
                        remote.title,
                        remote.description,
                        remote.pillar,
                        remote.validFrom,
                        remote.validTo,
                        remote.status.toDb(),
                        remote.createdBy,
                        remote.createdAt,
                        remote.updatedAt
                    )
                )
                remote
            }.recoverCatching {
                val local = guidelineDao.getById(id) ?: throw Exception("Diretriz não encontrada")
                Guideline(
                    local.id,
                    local.title,
                    local.description,
                    local.pillar,
                    local.validFrom,
                    local.validTo,
                    GuidelineStatus.fromDb(local.status),
                    local.createdBy,
                    local.createdAt,
                    local.updatedAt
                )
            }
        }

    override suspend fun getAll(): Result<List<Guideline>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val guidelines = guidelineApi.getAll().map { it.toDomain() }
                guidelineDao.deleteAll()
                guidelineDao.insertAll(guidelines.map {
                    GuidelineEntity(it.id, it.title, it.description, it.pillar,
                        it.validFrom, it.validTo, it.status.toDb(), it.createdBy,
                        it.createdAt, it.updatedAt)
                })
                guidelines
            }.recoverCatching {
                guidelineDao.getAll().map { e ->
                    Guideline(e.id, e.title, e.description, e.pillar,
                        e.validFrom, e.validTo, GuidelineStatus.fromDb(e.status),
                        e.createdBy, e.createdAt, e.updatedAt)
                }
            }
        }

    override suspend fun create(title: String, description: String, category: String, authorId: String): Result<Guideline> =
        withContext(Dispatchers.IO) {
            runCatching {
                val created = guidelineApi.create(
                    CreateGuidelineRequestDto(
                        title = title,
                        description = description,
                        category = category,
                        authorId = authorId
                    )
                ).toDomain()
                guidelineDao.insert(
                    GuidelineEntity(
                        created.id,
                        created.title,
                        created.description,
                        created.pillar,
                        created.validFrom,
                        created.validTo,
                        created.status.toDb(),
                        created.createdBy,
                        created.createdAt,
                        created.updatedAt
                    )
                )
                created
            }
        }

    override suspend fun update(id: String, title: String, description: String, category: String): Result<Guideline> =
        withContext(Dispatchers.IO) {
            runCatching {
                val updated = guidelineApi.update(
                    id,
                    UpdateGuidelineRequestDto(
                        title = title,
                        description = description,
                        category = category
                    )
                ).toDomain()
                guidelineDao.insert(
                    GuidelineEntity(
                        updated.id,
                        updated.title,
                        updated.description,
                        updated.pillar,
                        updated.validFrom,
                        updated.validTo,
                        updated.status.toDb(),
                        updated.createdBy,
                        updated.createdAt,
                        updated.updatedAt
                    )
                )
                updated
            }
        }

    override suspend fun delete(id: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                guidelineApi.delete(id)
                guidelineDao.deleteById(id)
            }
        }
}

