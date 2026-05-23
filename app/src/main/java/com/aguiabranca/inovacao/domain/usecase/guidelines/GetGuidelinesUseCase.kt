package com.aguiabranca.inovacao.domain.usecase.guidelines

import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import javax.inject.Inject

class GetGuidelinesUseCase @Inject constructor(
    private val repository: GuidelineRepository
) {
    suspend operator fun invoke(): Result<List<Guideline>> =
        repository.getAll()
}

