package com.aguiabranca.inovacao.ui.leadership

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.ui.components.AguiaProfileScreen
import com.aguiabranca.inovacao.ui.components.leadershipProfileConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileLeadershipViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    init { _user.value = sessionManager.getUser() }
}

@Composable
fun ProfileLeadershipScreen(
    onLogout: () -> Unit,
    viewModel: ProfileLeadershipViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    AguiaProfileScreen(
        user = user,
        config = leadershipProfileConfig(),
        onLogout = onLogout
    )
}

