package com.aguiabranca.inovacao.ui.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectEditUiState(
    val isLoading: Boolean = false,
    val project: Project? = null,
    val isSaving: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProjectEditViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectEditUiState())
    val uiState: StateFlow<ProjectEditUiState> = _uiState

    fun loadProject(projectId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            projectRepository.getById(projectId)
                .onSuccess { project ->
                    _uiState.value = _uiState.value.copy(isLoading = false, project = project)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar projeto."
                    )
                }
        }
    }

    fun updateProject(
        id: String,
        title: String,
        objective: String,
        stage: ProjectStage,
        status: ProjectStatus,
        investment: Double,
        financialReturn: Double,
        costReduction: Double,
        productivityGainPct: Double,
        progressPct: Int
    ) {
        if (title.isBlank() || objective.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Preencha título e objetivo.")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            projectRepository.update(
                id = id,
                title = title.trim(),
                objective = objective.trim(),
                stage = stage,
                status = status,
                investment = investment,
                financialReturn = financialReturn,
                costReduction = costReduction,
                productivityGainPct = productivityGainPct,
                progressPct = progressPct
            ).onSuccess {
                _uiState.value = _uiState.value.copy(isSaving = false, success = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Erro ao atualizar projeto."
                )
            }
        }
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            projectRepository.delete(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSaving = false, success = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Erro ao deletar projeto."
                    )
                }
        }
    }
}

