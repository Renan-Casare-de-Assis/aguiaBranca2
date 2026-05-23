package com.aguiabranca.inovacao.domain.model

data class Project(
    val id: String,
    val ideaId: String?,
    val title: String,
    val objective: String,
    val stage: ProjectStage,
    val status: ProjectStatus,
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

