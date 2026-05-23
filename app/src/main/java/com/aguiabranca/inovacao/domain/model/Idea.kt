package com.aguiabranca.inovacao.domain.model

data class Idea(
    val id: String,
    val title: String,
    val description: String,
    val category: IdeaCategory,
    val operatorId: String,
    val operatorName: String,
    val unit: String?,
    val status: IdeaStatus,
    val priority: IdeaPriority?,
    val managerComment: String?,
    val approvedBy: String?,
    val approvedAt: Long?,
    val createdAt: Long,
    val updatedAt: Long
)

