package com.aguiabranca.inovacao.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loggedUser: User? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Preencha e-mail e senha.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            userRepository.login(email.trim(), password.trim())
                .onSuccess { user ->
                    sessionManager.login(user)
                    _uiState.value = _uiState.value.copy(isLoading = false, loggedUser = user)
                }
                .onFailure { e ->
                    val friendlyMessage = when {
                        e.message.isNullOrBlank() -> "Erro ao fazer login."
                        e.message!!.contains("Oracle", ignoreCase = true) -> "Falha de conexão. Tente novamente."
                        e.message!!.contains("SQLException", ignoreCase = true) -> "Falha de conexão. Tente novamente."
                        e.message!!.contains("class", ignoreCase = true) -> "Falha de conexão. Tente novamente."
                        else -> e.message!!
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = friendlyMessage
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

