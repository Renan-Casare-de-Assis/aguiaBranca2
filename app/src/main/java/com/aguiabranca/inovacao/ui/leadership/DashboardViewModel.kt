package com.aguiabranca.inovacao.ui.leadership

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val metrics: DashboardMetrics? = null,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init { load() }

    fun load() {
        val user = sessionManager.getUser()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, user = user)
            dashboardRepository.getMetrics()
                .onSuccess { metrics ->
                    _uiState.value = _uiState.value.copy(isLoading = false, metrics = metrics)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar métricas."
                    )
                }
        }
    }
}

