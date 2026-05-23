package com.aguiabranca.inovacao.domain.model

data class Guideline(
    val id: String,
    val title: String,
    val description: String,
    val pillar: String,
    val validFrom: Long?,
    val validTo: Long?,
    val status: GuidelineStatus,
    val createdBy: String,
    val createdAt: Long,
    val updatedAt: Long
)

