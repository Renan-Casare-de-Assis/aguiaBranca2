package com.aguiabranca.inovacao.ui.operator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.ui.components.AguiaProfileScreen
import com.aguiabranca.inovacao.ui.components.operatorProfileConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileOperatorViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init { _user.value = sessionManager.getUser() }
}

@Composable
fun ProfileOperatorScreen(
    onLogout: () -> Unit,
    viewModel: ProfileOperatorViewModel = hiltViewModel()
) {
    val user = viewModel.user.collectAsState().value

    AguiaProfileScreen(
        user = user,
        config = operatorProfileConfig(),
        onLogout = onLogout
    )
}

