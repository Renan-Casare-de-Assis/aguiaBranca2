package com.aguiabranca.inovacao.ui.leadership

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.repository.GuidelineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GuidelinesCrudUiState(
    val isLoading: Boolean = false,
    val guidelines: List<Guideline> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class GuidelinesCrudViewModel @Inject constructor(
    private val guidelineRepository: GuidelineRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(GuidelinesCrudUiState())
    val uiState: StateFlow<GuidelinesCrudUiState> = _uiState

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
                        error = mapFriendlyError(e)
                    )
                }
        }
    }

    fun create(title: String, description: String, category: String) {
        val user = sessionManager.getUser() ?: return
        viewModelScope.launch {
            guidelineRepository.create(title.trim(), description.trim(), category, user.id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(successMessage = "Orientação criada!")
                    load()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = mapFriendlyError(e))
                }
        }
    }

    fun update(id: String, title: String, description: String, category: String) {
        viewModelScope.launch {
            guidelineRepository.update(id, title.trim(), description.trim(), category)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(successMessage = "Orientação atualizada!")
                    load()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = mapFriendlyError(e))
                }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            guidelineRepository.delete(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(successMessage = "Orientação removida.")
                    load()
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(error = mapFriendlyError(e))
                }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(successMessage = null, error = null)
    }

    private fun mapFriendlyError(error: Throwable): String {
        val raw = error.message.orEmpty()
        if (raw.contains("timeout", ignoreCase = true) ||
            raw.contains("refused", ignoreCase = true) ||
            raw.contains("UnknownHost", ignoreCase = true)
        ) {
            return "Falha de conexão com serviço remoto. Tente novamente em instantes."
        }
        return raw.ifBlank { "Erro ao processar orientação." }
    }
}

