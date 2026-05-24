package com.aguiabranca.backend.guideline

data class GuidelineResponse(
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

data class CreateGuidelineRequest(
    val title: String,
    val description: String,
    val category: String,
    val authorId: String
)

data class UpdateGuidelineRequest(
    val title: String,
    val description: String,
    val category: String
)

