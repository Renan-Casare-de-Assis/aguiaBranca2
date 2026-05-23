package com.aguiabranca.inovacao.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_guidelines")
data class GuidelineEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val pillar: String,
    val validFrom: Long?,
    val validTo: Long?,
    val status: String,
    val createdBy: String,
    val createdAt: Long,
    val updatedAt: Long
)

