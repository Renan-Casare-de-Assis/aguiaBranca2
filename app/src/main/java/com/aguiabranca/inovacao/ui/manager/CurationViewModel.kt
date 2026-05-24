package com.aguiabranca.inovacao.ui.manager

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

data class CurationUiState(
    val isLoading: Boolean = false,
    val ideas: List<Idea> = emptyList(),
    val error: String? = null,
    val actionSuccess: Boolean = false
)

@HiltViewModel
class CurationViewModel @Inject constructor(
    private val ideaRepository: IdeaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurationUiState())
    val uiState: StateFlow<CurationUiState> = _uiState

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            ideaRepository.getAll()
                .onSuccess { ideas ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        ideas = ideas.filter { it.status == IdeaStatus.NEW || it.status == IdeaStatus.UNDER_REVIEW }
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar ideias."
                    )
                }
        }
    }

    fun approve(ideaId: String, comment: String) {
        viewModelScope.launch {
            ideaRepository.updateStatus(ideaId, IdeaStatus.APPROVED, comment)
                .onSuccess { load() }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
        }
    }

    fun reject(ideaId: String, comment: String) {
        viewModelScope.launch {
            ideaRepository.updateStatus(ideaId, IdeaStatus.REJECTED, comment)
                .onSuccess { load() }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
        }
    }

    fun prioritize(ideaId: String, priority: IdeaPriority) {
        viewModelScope.launch {
            ideaRepository.updatePriority(ideaId, priority)
                .onSuccess { load() }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
        }
    }
}

