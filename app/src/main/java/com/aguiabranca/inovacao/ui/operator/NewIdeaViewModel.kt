package com.aguiabranca.inovacao.ui.operator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.IdeaCategory
import com.aguiabranca.inovacao.domain.repository.IdeaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewIdeaUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NewIdeaViewModel @Inject constructor(
    private val ideaRepository: IdeaRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewIdeaUiState())
    val uiState: StateFlow<NewIdeaUiState> = _uiState

    private fun rootCause(t: Throwable): Throwable {
        var current = t
        while (current.cause != null && current.cause !== current) {
            current = current.cause!!
        }
        return current
    }

    private fun friendlyError(error: Throwable): String {
        val root = rootCause(error)
        val details = buildString {
            append(error.message.orEmpty())
            append(" ")
            append(root.message.orEmpty())
            append(" ")
            append(error::class.java.name)
            append(" ")
            append(root::class.java.name)
            append(" ")
            append(error.stackTraceToString())
        }

        return when {
            Regex("ORA-\\d+").containsMatchIn(details) -> {
                val oraCode = Regex("ORA-\\d+").find(details)?.value ?: "ORA"
                "Erro no Oracle ($oraCode) retornado pela API. Verifique o backend."
            }
            details.contains("timeout", ignoreCase = true) ||
                details.contains("refused", ignoreCase = true) ||
                details.contains("UnknownHost", ignoreCase = true) ||
                details.contains("Network", ignoreCase = true) -> "Falha de rede ao conectar na API."
            details.isBlank() -> "Erro ao enviar ideia."
            else -> "Erro ao enviar ideia. ${root.message ?: "Sem detalhe"}"
        }
    }

    fun submit(title: String, description: String, category: IdeaCategory) {
        val user = sessionManager.getUser() ?: return
        if (title.isBlank() || description.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Preencha título e descrição.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            ideaRepository.create(
                title = title.trim(),
                description = description.trim(),
                category = category,
                operatorId = user.id,
                operatorName = user.name,
                unit = user.unit
            ).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = friendlyError(e)
                )
            }
        }
    }
}

