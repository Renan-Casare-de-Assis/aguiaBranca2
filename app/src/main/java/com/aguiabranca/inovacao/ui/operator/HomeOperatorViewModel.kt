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

data class HomeOperatorUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val myIdeas: List<Idea> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeOperatorViewModel @Inject constructor(
    private val ideaRepository: IdeaRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeOperatorUiState())
    val uiState: StateFlow<HomeOperatorUiState> = _uiState

    init {
        load()
    }

    fun load() {
        val user = sessionManager.getUser() ?: return
        _uiState.value = _uiState.value.copy(isLoading = true, user = user)
        viewModelScope.launch {
            ideaRepository.getByOperator(user.id)
                .onSuccess { ideas ->
                    _uiState.value = _uiState.value.copy(isLoading = false, myIdeas = ideas)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }
}

