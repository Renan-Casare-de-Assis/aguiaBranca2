package com.aguiabranca.backend.idea

data class IdeaResponse(
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

data class CreateIdeaRequest(
    val title: String,
    val description: String,
    val category: String,
    val operatorId: String,
    val operatorName: String,
    val unit: String? = null
)

data class UpdateIdeaStatusRequest(
    val status: String,
    val comment: String? = null
)

data class UpdateIdeaPriorityRequest(
    val priority: String
)

