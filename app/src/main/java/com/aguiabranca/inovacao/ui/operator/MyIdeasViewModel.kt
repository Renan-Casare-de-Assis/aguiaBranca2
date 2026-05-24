package com.aguiabranca.inovacao.ui.operator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyIdeasUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val ideas: List<Idea> = emptyList(),
    val error: String? = null,
    val deleteInProgress: String? = null
)

@HiltViewModel
class MyIdeasViewModel @Inject constructor(
    private val ideaRepository: IdeaRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyIdeasUiState())
    val uiState: StateFlow<MyIdeasUiState> = _uiState

    init { load() }

    fun load() {
        val user = sessionManager.getUser() ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, user = user, error = null)
        viewModelScope.launch {
            ideaRepository.getByOperator(user.id)
                .onSuccess { ideas ->
                    _uiState.value = _uiState.value.copy(isLoading = false, ideas = ideas)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar ideias."
                    )
                }
        }
    }

    fun deleteIdea(ideaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(deleteInProgress = ideaId)
            ideaRepository.delete(ideaId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        ideas = _uiState.value.ideas.filterNot { it.id == ideaId },
                        deleteInProgress = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Erro ao deletar ideia.",
                        deleteInProgress = null
                    )
                }
        }
    }
}
