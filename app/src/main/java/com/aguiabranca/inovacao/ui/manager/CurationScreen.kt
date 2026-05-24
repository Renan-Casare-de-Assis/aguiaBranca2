package com.aguiabranca.inovacao.ui.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaCard
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.StatusChip
import com.aguiabranca.inovacao.ui.theme.Dourado
import com.aguiabranca.inovacao.ui.theme.VerdeSuccess

@Composable
fun CurationScreen(
    onNavigateToProjectForm: (String?) -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToGuidelines: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: CurationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { AguiaTopBar(title = "Curadoria de Ideias") },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Curadoria", "Projetos", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> {}
                        1 -> onNavigateToProjects()
                        2 -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Dourado
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.load() }) {
                            Text("Tentar novamente", color = Dourado)
                        }
                    }
                }
                uiState.ideas.isEmpty() -> {
                    Text(
                        text = "Nenhuma ideia aguardando curadoria.",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.ideas) { idea ->
                            CurationIdeaCard(
                                idea = idea,
                                onApprove = { comment -> viewModel.approve(idea.id, comment) },
                                onReject = { comment -> viewModel.reject(idea.id, comment) },
                                onPrioritize = { priority -> viewModel.prioritize(idea.id, priority) },
                                onCreateProject = { onNavigateToProjectForm(idea.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurationIdeaCard(
    idea: Idea,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onPrioritize: (IdeaPriority) -> Unit,
    onCreateProject: () -> Unit
) {
    var comment by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AguiaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = idea.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(
                        text = "Por: ${idea.operatorName}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                StatusChip(status = idea.status.name)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = idea.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentário (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onApprove(comment) },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeSuccess),
                    modifier = Modifier.weight(1f)
                ) { Text("Aprovar", fontSize = 12.sp) }

                Button(
                    onClick = { onReject(comment) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) { Text("Rejeitar", fontSize = 12.sp) }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onPrioritize(IdeaPriority.HIGH) },
                    modifier = Modifier.weight(1f)
                ) { Text("⬆ Alta", fontSize = 12.sp, color = Dourado) }

                OutlinedButton(
                    onClick = onCreateProject,
                    modifier = Modifier.weight(1f)
                ) { Text("📁 Projeto", fontSize = 12.sp, color = Dourado) }
            }
        }
    }
}

