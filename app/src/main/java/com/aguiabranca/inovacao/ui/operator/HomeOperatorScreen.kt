package com.aguiabranca.inovacao.ui.operator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaCard
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.StatusChip
import com.aguiabranca.inovacao.ui.theme.AzulMarinho
import com.aguiabranca.inovacao.ui.theme.Dourado
import com.aguiabranca.inovacao.ui.theme.VerdeSuccess

@Composable
fun HomeOperatorScreen(
    onNavigateToNewIdea: () -> Unit,
    onNavigateToMyIdeas: () -> Unit,
    onNavigateToGuidelines: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeOperatorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            AguiaTopBar(title = "Início")
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Início", "Ideias", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> {}
                        1 -> onNavigateToMyIdeas()
                        2 -> onNavigateToProfile()
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewIdea,
                containerColor = Dourado
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova Ideia", tint = AzulMarinho)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Boas-vindas
            item {
                uiState.user?.let { user ->
                    Text(
                        text = "Olá, ${user.name.split(" ").first()} 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Suas ideias fazem a diferença!",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // KPIs resumo
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiMiniCard(
                        label = "Minhas Ideias",
                        value = "${uiState.myIdeas.size}",
                        color = Dourado,
                        modifier = Modifier.weight(1f)
                    )
                    KpiMiniCard(
                        label = "Aprovadas",
                        value = "${uiState.myIdeas.count { it.status.name == "APPROVED" }}",
                        color = VerdeSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Atalho orientações
            item {
                OutlinedButton(
                    onClick = onNavigateToGuidelines,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Dourado),
                    border = ButtonDefaults.outlinedButtonBorder.copy()
                ) {
                    Text("📋  Ver Orientações Estratégicas")
                }
            }

            // Últimas ideias
            item {
                Text(
                    text = "Minhas últimas ideias",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Dourado)
                    }
                }
            } else if (uiState.myIdeas.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma ideia enviada ainda. Toque em + para começar!",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                }
            } else {
                items(uiState.myIdeas.take(3)) { idea ->
                    IdeaSummaryCard(idea = idea)
                }
                if (uiState.myIdeas.size > 3) {
                    item {
                        TextButton(onClick = onNavigateToMyIdeas) {
                            Text("Ver todas as ideias →", color = Dourado)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiMiniCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    AguiaCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun IdeaSummaryCard(idea: Idea) {
    AguiaCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = idea.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(status = idea.status.name)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = idea.description.take(80) + if (idea.description.length > 80) "..." else "",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
