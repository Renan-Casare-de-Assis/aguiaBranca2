package com.aguiabranca.inovacao.data.local.room.dao

import androidx.room.*
import com.aguiabranca.inovacao.data.local.room.entity.ProjectEntity

@Dao
interface ProjectDao {
    @Query("SELECT * FROM cached_projects ORDER BY createdAt DESC")
    suspend fun getAll(): List<ProjectEntity>

    @Query("SELECT * FROM cached_projects WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: ProjectEntity)

    @Query("DELETE FROM cached_projects")
    suspend fun deleteAll()

    @Query("DELETE FROM cached_projects WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(project: ProjectEntity)
}
