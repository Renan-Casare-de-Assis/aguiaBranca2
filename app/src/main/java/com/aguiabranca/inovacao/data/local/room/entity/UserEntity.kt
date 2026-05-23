package com.aguiabranca.inovacao.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val role: String,
    val area: String?,
    val active: Boolean
)

