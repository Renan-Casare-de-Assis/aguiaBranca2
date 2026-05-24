package com.aguiabranca.inovacao.data.remote.api

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val unit: String?,
    val avatarUrl: String?,
    val createdAt: Long
)

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class ProjectDto(
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

data class CreateProjectRequestDto(
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

data class UpdateProjectRequestDto(
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

data class IdeaDto(
    val id: String,
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

data class CreateIdeaRequestDto(
    val title: String,
    val description: String,
    val category: String,
    val operatorId: String,
    val operatorName: String,
    val unit: String?
)

data class UpdateIdeaStatusRequestDto(
    val status: String,
    val comment: String?
)

data class UpdateIdeaPriorityRequestDto(
    val priority: String
)

data class GuidelineDto(
    val id: String,
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

data class CreateGuidelineRequestDto(
    val title: String,
    val description: String,
    val category: String,
    val authorId: String
)

data class UpdateGuidelineRequestDto(
    val title: String,
    val description: String,
    val category: String
)

data class DashboardMetricsDto(
    val totalIdeas: Int,
    val approvedIdeas: Int,
    val activeProjects: Int,
    val totalInvestment: Double,
    val totalReturn: Double,
    val totalProfit: Double,
    val totalCostReduction: Double,
    val avgProductivityGainPct: Double,
    val roiGlobal: Double
)

