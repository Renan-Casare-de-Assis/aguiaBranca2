package com.aguiabranca.inovacao.ui.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectsUiState(
    val isLoading: Boolean = false,
    val projects: List<Project> = emptyList(),
    val error: String? = null,
    val deleteInProgress: String? = null
)

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            projectRepository.getAll()
                .onSuccess { projects ->
                    _uiState.value = _uiState.value.copy(isLoading = false, projects = projects)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar projetos."
                    )
                }
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(deleteInProgress = projectId)
            projectRepository.delete(projectId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        projects = _uiState.value.projects.filterNot { it.id == projectId },
                        deleteInProgress = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Erro ao deletar projeto.",
                        deleteInProgress = null
                    )
                }
        }
    }
}

