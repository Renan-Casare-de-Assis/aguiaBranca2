package com.aguiabranca.inovacao.ui.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GuidelinesManagerUiState(
    val isLoading: Boolean = false,
    val guidelines: List<Guideline> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class GuidelinesManagerViewModel @Inject constructor(
    private val guidelineRepository: GuidelineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GuidelinesManagerUiState())
    val uiState: StateFlow<GuidelinesManagerUiState> = _uiState

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            guidelineRepository.getAll()
                .onSuccess { list ->
                    _uiState.value = _uiState.value.copy(isLoading = false, guidelines = list)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar orientações."
                    )
                }
        }
    }
}

