package com.aguiabranca.inovacao.data.local.room.dao

import androidx.room.*
import com.aguiabranca.inovacao.data.local.room.entity.GuidelineEntity

@Dao
interface GuidelineDao {
    @Query("SELECT * FROM cached_guidelines ORDER BY createdAt DESC")
    suspend fun getAll(): List<GuidelineEntity>

    @Query("SELECT * FROM cached_guidelines WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): GuidelineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(guidelines: List<GuidelineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guideline: GuidelineEntity)

    @Query("DELETE FROM cached_guidelines WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM cached_guidelines")
    suspend fun deleteAll()
}

