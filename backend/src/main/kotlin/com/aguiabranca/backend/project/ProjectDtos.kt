package com.aguiabranca.backend.project

data class ProjectResponse(
    val id: String,
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

data class CreateProjectRequest(
    val ideaId: String?,
    val title: String,
    val objective: String,
    val stage: String,
    val status: String,
    val managerId: String,
    val startDate: Long,
    val targetEndDate: Long,
    val investment: Double
)

data class UpdateProjectRequest(
    val title: String,
    val objective: String,
    val stage: String,
    val status: String,
    val investment: Double,
    val financialReturn: Double,
    val costReduction: Double,
    val productivityGainPct: Double,
    val progressPct: Int
)

