package com.aguiabranca.inovacao.ui.operator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IdeaEditUiState(
    val isLoading: Boolean = false,
    val idea: Idea? = null,
    val isSaving: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class IdeaEditViewModel @Inject constructor(
    private val ideaRepository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IdeaEditUiState())
    val uiState: StateFlow<IdeaEditUiState> = _uiState

    fun loadIdea(ideaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            ideaRepository.getById(ideaId)
                .onSuccess { idea ->
                    _uiState.value = _uiState.value.copy(isLoading = false, idea = idea)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar ideia."
                    )
                }
        }
    }

    fun updateStatus(id: String, status: IdeaStatus, comment: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            ideaRepository.updateStatus(id, status, comment)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSaving = false, success = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Erro ao atualizar status."
                    )
                }
        }
    }

    fun updatePriority(id: String, priority: IdeaPriority) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            ideaRepository.updatePriority(id, priority)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSaving = false, success = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Erro ao atualizar prioridade."
                    )
                }
        }
    }

    fun deleteIdea(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            ideaRepository.delete(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isSaving = false, success = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Erro ao deletar ideia."
                    )
                }
        }
    }
}

