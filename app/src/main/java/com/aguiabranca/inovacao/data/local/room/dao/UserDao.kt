package com.aguiabranca.inovacao.data.local.room.dao

import androidx.room.*
import com.aguiabranca.inovacao.data.local.room.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM cached_users")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM cached_users WHERE uid = :uid LIMIT 1")
    suspend fun getById(uid: String): UserEntity?

    @Query("SELECT * FROM cached_users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM cached_users")
    suspend fun deleteAll()
}

