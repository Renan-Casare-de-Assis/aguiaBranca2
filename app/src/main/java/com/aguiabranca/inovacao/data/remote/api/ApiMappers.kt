package com.aguiabranca.inovacao.data.remote.api

import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.GuidelineStatus
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaCategory
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.model.UserRole

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    role = UserRole.fromDb(role),
    unit = unit,
    avatarUrl = avatarUrl,
    createdAt = createdAt
)

fun ProjectDto.toDomain(): Project = Project(
    id = id,
    ideaId = ideaId,
    title = title,
    objective = objective,
    stage = ProjectStage.fromDb(stage),
    status = ProjectStatus.fromDb(status),
    managerId = managerId,
    startDate = startDate,
    targetEndDate = targetEndDate,
    actualEndDate = actualEndDate,
    investment = investment,
    financialReturn = financialReturn,
    costReduction = costReduction,
    productivityGainPct = productivityGainPct,
    profit = profit,
    roi = roi,
    progressPct = progressPct,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun IdeaDto.toDomain(): Idea = Idea(
    id = id,
    title = title,
    description = description,
    category = IdeaCategory.fromDb(category),
    operatorId = operatorId,
    operatorName = operatorName,
    unit = unit,
    status = IdeaStatus.fromDb(status),
    priority = IdeaPriority.fromDb(priority),
    managerComment = managerComment,
    approvedBy = approvedBy,
    approvedAt = approvedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun GuidelineDto.toDomain(): Guideline = Guideline(
    id = id,
    title = title,
    description = description,
    pillar = pillar,
    validFrom = validFrom,
    validTo = validTo,
    status = GuidelineStatus.fromDb(status),
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DashboardMetricsDto.toDomain(): DashboardMetrics = DashboardMetrics(
    totalIdeas = totalIdeas,
    approvedIdeas = approvedIdeas,
    activeProjects = activeProjects,
    totalInvestment = totalInvestment,
    totalReturn = totalReturn,
    totalProfit = totalProfit,
    totalCostReduction = totalCostReduction,
    avgProductivityGainPct = avgProductivityGainPct,
    roiGlobal = roiGlobal
)

