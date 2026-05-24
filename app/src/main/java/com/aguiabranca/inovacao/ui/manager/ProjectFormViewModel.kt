package com.aguiabranca.inovacao.ui.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectFormUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProjectFormViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectFormUiState())
    val uiState: StateFlow<ProjectFormUiState> = _uiState

    fun submit(
        ideaId: String?,
        title: String,
        objective: String,
        investment: String,
        startDate: Long,
        targetEndDate: Long
    ) {
        val user = sessionManager.getUser() ?: return
        if (title.isBlank() || objective.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Preencha título e objetivo.")
            return
        }
        val investmentValue = investment.toDoubleOrNull() ?: 0.0
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            projectRepository.create(
                ideaId = ideaId,
                title = title.trim(),
                objective = objective.trim(),
                stage = ProjectStage.DISCOVERY,
                status = ProjectStatus.ON_TRACK,
                managerId = user.id,
                startDate = startDate,
                targetEndDate = targetEndDate,
                investment = investmentValue
            ).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erro ao criar projeto."
                )
            }
        }
    }
}


