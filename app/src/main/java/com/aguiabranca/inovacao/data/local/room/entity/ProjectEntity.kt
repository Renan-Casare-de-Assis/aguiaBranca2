package com.aguiabranca.inovacao.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val ideaId: String?,
    val title: String,
    val objective: String,
    val stage: String,
    val status: String,
    val managerId: String,
    val startDate: Long,
    val targetEndDate: Long,
    val actualEndDate: Long?,
    val investment: Double,
    val financialReturn: Double,
    val costReduction: Double,
    val productivityGainPct: Double,
    val profit: Double,
    val roi: Double,
    val progressPct: Int,
    val createdAt: Long,
    val updatedAt: Long
)

