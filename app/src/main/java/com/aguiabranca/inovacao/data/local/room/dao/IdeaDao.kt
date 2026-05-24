package com.aguiabranca.inovacao.data.local.room.dao

import androidx.room.*
import com.aguiabranca.inovacao.data.local.room.entity.IdeaEntity

@Dao
interface IdeaDao {
    @Query("SELECT * FROM cached_ideas ORDER BY createdAt DESC")
    suspend fun getAll(): List<IdeaEntity>

    @Query("SELECT * FROM cached_ideas WHERE operatorId = :operatorId ORDER BY createdAt DESC")
    suspend fun getByOperator(operatorId: String): List<IdeaEntity>

    @Query("SELECT * FROM cached_ideas WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): IdeaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ideas: List<IdeaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(idea: IdeaEntity)

    @Query("DELETE FROM cached_ideas")
    suspend fun deleteAll()

    @Query("DELETE FROM cached_ideas WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM cached_ideas WHERE operatorId = :operatorId")
    suspend fun deleteByOperator(operatorId: String)

    @Update
    suspend fun update(idea: IdeaEntity)
}
