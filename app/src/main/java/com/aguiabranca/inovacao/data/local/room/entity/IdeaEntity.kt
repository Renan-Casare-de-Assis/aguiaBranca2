package com.aguiabranca.inovacao.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_ideas")
data class IdeaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val operatorId: String,
    val operatorName: String,
    val unit: String?,
    val status: String,
    val priority: String?,
    val managerComment: String?,
    val approvedBy: String?,
    val approvedAt: Long?,
    val createdAt: Long,
    val updatedAt: Long
)

