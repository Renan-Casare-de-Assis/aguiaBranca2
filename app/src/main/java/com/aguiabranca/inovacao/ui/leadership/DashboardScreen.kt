package com.aguiabranca.inovacao.ui.leadership

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaCard
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.theme.Dourado
import com.aguiabranca.inovacao.ui.theme.VerdeSuccess

@Composable
fun DashboardScreen(
    onNavigateToPortfolio: () -> Unit,
    onNavigateToGuidelines: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { AguiaTopBar(title = "Dashboard") },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Dashboard", "Orientações", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> {}
                        1 -> onNavigateToGuidelines()
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
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            uiState.user?.let { user ->
                                Text(
                                    text = "Olá, ${user.name.split(" ").first()} 👋",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Visão geral da inovação",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }

                        uiState.metrics?.let { metrics ->
                            item {
                                Text(
                                    text = "📊 Indicadores",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    DashKpiCard(
                                        label = "Total Ideias",
                                        value = "${metrics.totalIdeas}",
                                        color = Dourado,
                                        modifier = Modifier.weight(1f)
                                    )
                                    DashKpiCard(
                                        label = "Aprovadas",
                                        value = "${metrics.approvedIdeas}",
                                        color = VerdeSuccess,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    DashKpiCard(
                                        label = "Projetos Ativos",
                                        value = "${metrics.activeProjects}",
                                        color = Dourado,
                                        modifier = Modifier.weight(1f)
                                    )
                                    DashKpiCard(
                                        label = "ROI Global",
                                        value = "${"%.1f".format(metrics.roiGlobal)}%",
                                        color = VerdeSuccess,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            item {
                                Text(
                                    text = "💰 Financeiro",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                DashFinanceCard(metrics = metrics)
                            }

                            item {
                                OutlinedButton(
                                    onClick = onNavigateToPortfolio,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Dourado)
                                ) {
                                    Text("📁  Ver Portfólio de Projetos")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashKpiCard(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    AguiaCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = color)
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun DashFinanceCard(metrics: DashboardMetrics) {
    AguiaCard {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FinanceRow("Investimento Total", "R$ ${"%.2f".format(metrics.totalInvestment)}")
            FinanceRow("Retorno Total",      "R$ ${"%.2f".format(metrics.totalReturn)}")
            FinanceRow("Lucro Total",        "R$ ${"%.2f".format(metrics.totalProfit)}")
            FinanceRow("Redução de Custo",   "R$ ${"%.2f".format(metrics.totalCostReduction)}")
            FinanceRow("Ganho Produtividade","${"%.1f".format(metrics.avgProductivityGainPct)}%")
        }
    }
}

@Composable
private fun FinanceRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Dourado)
    }
}

