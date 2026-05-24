package com.aguiabranca.inovacao.ui.leadership

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.DashboardMetrics
import com.aguiabranca.inovacao.ui.components.AguiaBadge
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.BadgeVariant
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Danger500
import com.aguiabranca.inovacao.ui.theme.Gray100
import com.aguiabranca.inovacao.ui.theme.Gray300
import com.aguiabranca.inovacao.ui.theme.Gray500
import com.aguiabranca.inovacao.ui.theme.ProfilePrimaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSecondaryText
import com.aguiabranca.inovacao.ui.theme.Success500
import com.aguiabranca.inovacao.ui.theme.White
import java.text.DecimalFormat

@Composable
fun DashboardScreen(
    onNavigateToPortfolio: () -> Unit,
    onNavigateToGuidelines: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val initials = uiState.user?.name
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.take(2)
        ?.joinToString("") { it.first().uppercaseChar().toString() }
        .orEmpty()

    val metrics = uiState.metrics ?: previewMetrics()

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Dashboard",
                subtitle = "Visão consolidada da inovação corporativa",
                notificationCount = 0,
                userInitials = if (initials.isBlank()) "RD" else initials
            )
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Dashboard", "Orientações", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> Unit
                        1 -> onNavigateToGuidelines()
                        2 -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF4F6FA)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    AguiaBadge(text = "Mai 2026", variant = BadgeVariant.INFO)
                }
            }

            if (uiState.isLoading) {
                item {
                    CenterCard {
                        CircularProgressIndicator(color = Brand700, modifier = Modifier.size(30.dp))
                    }
                }
            }

            if (uiState.error != null) {
                item {
                    CenterCard {
                        Text(
                            text = uiState.error ?: "Erro ao carregar dashboard.",
                            color = Danger500,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        TextButton(onClick = { viewModel.load() }) {
                            Text("Tentar novamente", color = Brand700)
                        }
                    }
                }
            }

            item {
                KpiGrid(metrics = metrics)
            }

            item {
                RoiChartCard()
            }

            item {
                ProjectImpactCard(metrics = metrics)
            }

            item {
                Button(
                    onClick = onNavigateToPortfolio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Ver Projetos", color = White, fontWeight = FontWeight.Bold)
                            Text("8 projetos ativos • 5 on track", color = White.copy(alpha = 0.8f), fontSize = 11.sp)
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiGrid(metrics: DashboardMetrics) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KpiMetricCard(
                title = "INVESTIMENTO",
                value = compactMoney(metrics.totalInvestment),
                containerColor = Color(0xFF243B67),
                modifier = Modifier.weight(1f)
            )
            KpiMetricCard(
                title = "RETORNO",
                value = compactMoney(metrics.totalReturn),
                containerColor = Color(0xFF32B259),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KpiMetricCard(
                title = "REDUCAO",
                value = compactMoney(metrics.totalCostReduction),
                containerColor = Color(0xFFC8AA47),
                modifier = Modifier.weight(1f)
            )
            KpiMetricCard(
                title = "% ROI GLOBAL",
                value = "${formatPercent(metrics.roiGlobal)}",
                containerColor = Color(0xFF313A4A),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KpiMetricCard(
    title: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, color = White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            Text(value, color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
private fun RoiChartCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Assessment, contentDescription = null, tint = Gray500, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ROI por Projeto", color = ProfilePrimaryText, fontWeight = FontWeight.SemiBold)
                }
                AguiaBadge(text = "+12% mai", variant = BadgeVariant.SUCCESS)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Gray100, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                listOf(44.dp, 65.dp, 84.dp, 56.dp, 94.dp, 70.dp).forEach { h ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(h)
                            .background(Color(0xFF546A93), RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectImpactCard(metrics: DashboardMetrics) {
    val progress = (metrics.avgProductivityGainPct / 25.0).coerceIn(0.1, 1.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Triagem digital de embarque",
                    color = ProfilePrimaryText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                AguiaBadge(text = "Concluido", variant = BadgeVariant.SUCCESS)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ROI ${formatPercent(metrics.roiGlobal)}", color = Gray500, fontSize = 12.sp)
                }
                AguiaBadge(text = "+${formatPercent(metrics.avgProductivityGainPct)} prod.", variant = BadgeVariant.SUCCESS)
            }

            LinearProgressSlim(progress = progress.toFloat())

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Prazo: 5 meses", color = Gray500, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun LinearProgressSlim(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Gray300, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(8.dp)
                .background(Success500, RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun CenterCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

private fun previewMetrics(): DashboardMetrics = DashboardMetrics(
    totalIdeas = 84,
    approvedIdeas = 51,
    activeProjects = 8,
    totalInvestment = 2_400_000.0,
    totalReturn = 4_100_000.0,
    totalProfit = 1_700_000.0,
    totalCostReduction = 980_000.0,
    avgProductivityGainPct = 18.0,
    roiGlobal = 71.0
)

private fun compactMoney(value: Double): String {
    val abs = kotlin.math.abs(value)
    return when {
        abs >= 1_000_000 -> {
            val num = DecimalFormat("0.0").format(value / 1_000_000)
            "R$${num}M"
        }
        abs >= 1_000 -> {
            val num = DecimalFormat("0").format(value / 1_000)
            "R$${num}k"
        }
        else -> "R$${DecimalFormat("0").format(value)}"
    }
}

private fun formatPercent(value: Double): String = DecimalFormat("0").format(value) + "%"
