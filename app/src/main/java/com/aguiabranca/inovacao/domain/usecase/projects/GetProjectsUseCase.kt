package com.aguiabranca.inovacao.domain.usecase.projects

import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): Result<List<Project>> =
        repository.getAll()
}

