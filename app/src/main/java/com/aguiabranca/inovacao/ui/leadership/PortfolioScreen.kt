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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.Project
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.ui.components.AguiaBadge
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.BadgeVariant
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Danger500
import com.aguiabranca.inovacao.ui.theme.Gray100
import com.aguiabranca.inovacao.ui.theme.Gray300
import com.aguiabranca.inovacao.ui.theme.Gray400
import com.aguiabranca.inovacao.ui.theme.Gray500
import com.aguiabranca.inovacao.ui.theme.Gray600
import com.aguiabranca.inovacao.ui.theme.ProfilePrimaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSecondaryText
import com.aguiabranca.inovacao.ui.theme.Success500
import com.aguiabranca.inovacao.ui.theme.Warning500
import com.aguiabranca.inovacao.ui.theme.White
import java.text.DecimalFormat

private enum class PortfolioFilter {
    ALL,
    ON_TRACK,
    AT_RISK
}

@Composable
fun PortfolioScreen(
    onBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = onBack,
    onNavigateToGuidelines: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by rememberSaveable { mutableIntStateOf(1) }
    var selectedFilter by rememberSaveable { mutableStateOf(PortfolioFilter.ALL) }

    val displayProjects = if (uiState.projects.isEmpty()) previewProjects() else uiState.projects

    val filteredProjects = remember(displayProjects, selectedFilter) {
        displayProjects.filter { project ->
            when (selectedFilter) {
                PortfolioFilter.ALL -> true
                PortfolioFilter.ON_TRACK -> project.status == ProjectStatus.ON_TRACK
                PortfolioFilter.AT_RISK -> project.status == ProjectStatus.AT_RISK || project.status == ProjectStatus.DELAYED
            }
        }
    }

    val activeCount = displayProjects.count { it.status != ProjectStatus.CANCELED }
    val onTrackCount = displayProjects.count { it.status == ProjectStatus.ON_TRACK }
    val completedCount = displayProjects.count { it.status == ProjectStatus.COMPLETED }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Portfólio",
                subtitle = "Acompanhamento executivo de projetos",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = White
                        )
                    }
                },
                userInitials = "RD"
            )
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Dashboard", "Orientações", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> onNavigateToDashboard()
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(1.dp))
                    AguiaBadge(text = "Mai 2026", variant = BadgeVariant.INFO)
                }
            }

            if (uiState.isLoading && uiState.projects.isNotEmpty()) {
                item {
                    CenterInfoCard {
                        CircularProgressIndicator(color = Brand700, modifier = Modifier.size(30.dp))
                    }
                }
            }

            if (uiState.error != null) {
                item {
                    CenterInfoCard {
                        Text(
                            text = uiState.error ?: "Erro ao carregar portfólio.",
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
                PortfolioHeaderSummary(
                    activeCount = activeCount,
                    onTrackCount = onTrackCount,
                    completedCount = completedCount
                )
            }

            item {
                FilterRow(
                    selectedFilter = selectedFilter,
                    total = displayProjects.size,
                    onTrack = onTrackCount,
                    atRisk = displayProjects.count { it.status == ProjectStatus.AT_RISK || it.status == ProjectStatus.DELAYED },
                    onSelect = { selectedFilter = it }
                )
            }

            if (uiState.isLoading && uiState.projects.isEmpty()) {
                item {
                    CenterInfoCard {
                        CircularProgressIndicator(color = Brand700, modifier = Modifier.size(30.dp))
                    }
                }
            } else {
                items(filteredProjects, key = { it.id }) { project ->
                    PortfolioProjectCard(project = project)
                }

                if (filteredProjects.isEmpty()) {
                    item {
                        CenterInfoCard {
                            Text(
                                text = "Nenhum projeto para o filtro selecionado.",
                                color = ProfileSecondaryText,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item {
                FinancialConsolidationCard(projects = displayProjects)
            }
        }
    }
}

@Composable
private fun PortfolioHeaderSummary(
    activeCount: Int,
    onTrackCount: Int,
    completedCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCell(value = activeCount.toString(), label = "ATIVOS", modifier = Modifier.weight(1f))
            VerticalDivider()
            SummaryCell(value = onTrackCount.toString(), label = "ON TRACK", modifier = Modifier.weight(1f))
            VerticalDivider()
            SummaryCell(value = completedCount.toString(), label = "COMPLETOS", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryCell(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = ProfilePrimaryText, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(label, color = Gray500, fontSize = 10.sp)
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(54.dp)
            .background(Gray300)
    )
}

@Composable
private fun FilterRow(
    selectedFilter: PortfolioFilter,
    total: Int,
    onTrack: Int,
    atRisk: Int,
    onSelect: (PortfolioFilter) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChipButton(
            text = "Todos ($total)",
            selected = selectedFilter == PortfolioFilter.ALL,
            icon = Icons.Default.Assessment,
            onClick = { onSelect(PortfolioFilter.ALL) }
        )
        FilterChipButton(
            text = "On Track${if (onTrack > 0) " ($onTrack)" else ""}",
            selected = selectedFilter == PortfolioFilter.ON_TRACK,
            icon = Icons.Default.CheckCircleOutline,
            onClick = { onSelect(PortfolioFilter.ON_TRACK) }
        )
        FilterChipButton(
            text = "At Risk${if (atRisk > 0) " ($atRisk)" else ""}",
            selected = selectedFilter == PortfolioFilter.AT_RISK,
            icon = Icons.Default.WarningAmber,
            onClick = { onSelect(PortfolioFilter.AT_RISK) }
        )
    }
}

@Composable
private fun FilterChipButton(
    text: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) White else Color.Transparent,
            contentColor = if (selected) Brand700 else Gray600
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) Gray300 else Gray300),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
private fun PortfolioProjectCard(project: Project) {
    val progress = project.progressPct.coerceIn(0, 100)
    val progressColor = when (project.status) {
        ProjectStatus.ON_TRACK, ProjectStatus.COMPLETED -> Success500
        ProjectStatus.AT_RISK -> Warning500
        ProjectStatus.DELAYED -> Danger500
        ProjectStatus.CANCELED -> Gray400
    }

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
                    text = project.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = ProfilePrimaryText,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                AguiaBadge(
                    text = projectStatusTag(project.status),
                    variant = projectStatusBadge(project.status)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ROI ${formatPercent(project.roi)}", color = Gray500, fontSize = 12.sp)
                }
                AguiaBadge(
                    text = "+${formatPercent(project.productivityGainPct)} prod.",
                    variant = BadgeVariant.SUCCESS
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${progress}% concluído", color = Gray500, fontSize = 12.sp)
                Text(stageTimeLabel(project.stage), color = Gray500, fontSize = 12.sp)
            }

            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = progressColor,
                trackColor = Gray300
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${compactMoney(project.investment)} invest. • ROI projetado: ${formatPercent(project.roi)}",
                    color = Gray500,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun FinancialConsolidationCard(projects: List<Project>) {
    val investment = projects.sumOf { it.investment }
    val returned = projects.sumOf { it.financialReturn }
    val profit = projects.sumOf { it.profit }
    val avgRoi = if (projects.isNotEmpty()) projects.map { it.roi }.average() else 0.0

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Assessment, contentDescription = null, tint = Gray500, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Consolidado Financeiro", color = ProfilePrimaryText, fontWeight = FontWeight.SemiBold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FinanceItem(label = "Investimento Total", value = compactMoney(investment), modifier = Modifier.weight(1f))
                FinanceItem(label = "Retorno Acumulado", value = compactMoney(returned), valueColor = Success500, modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FinanceItem(label = "Lucro Global", value = compactMoney(profit), valueColor = Success500, modifier = Modifier.weight(1f))
                FinanceItem(label = "ROI Médio", value = formatPercent(avgRoi), modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FinanceItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = ProfilePrimaryText
) {
    Column(modifier = modifier) {
        Text(label, color = Gray500, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 30.sp)
    }
}

@Composable
private fun CenterInfoCard(content: @Composable ColumnScope.() -> Unit) {
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

private fun previewProjects(): List<Project> = listOf(
    Project(
        id = "portfolio_preview_1",
        ideaId = null,
        title = "Triagem digital de embarque",
        objective = "Automatizar conferência",
        stage = ProjectStage.EXECUTION,
        status = ProjectStatus.COMPLETED,
        managerId = "m1",
        startDate = System.currentTimeMillis(),
        targetEndDate = System.currentTimeMillis(),
        actualEndDate = null,
        investment = 180_000.0,
        financialReturn = 330_000.0,
        costReduction = 120_000.0,
        productivityGainPct = 18.0,
        profit = 150_000.0,
        roi = 84.0,
        progressPct = 100,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    ),
    Project(
        id = "portfolio_preview_2",
        ideaId = null,
        title = "QR Code para rastreamento de carga",
        objective = "Monitoramento operacional",
        stage = ProjectStage.EXECUTION,
        status = ProjectStatus.ON_TRACK,
        managerId = "m1",
        startDate = System.currentTimeMillis(),
        targetEndDate = System.currentTimeMillis(),
        actualEndDate = null,
        investment = 95_000.0,
        financialReturn = 140_000.0,
        costReduction = 65_000.0,
        productivityGainPct = 12.0,
        profit = 45_000.0,
        roi = 42.0,
        progressPct = 65,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    ),
    Project(
        id = "portfolio_preview_3",
        ideaId = null,
        title = "Manutenção preditiva de frota",
        objective = "Reduzir paradas não planejadas",
        stage = ProjectStage.PLANNING,
        status = ProjectStatus.AT_RISK,
        managerId = "m1",
        startDate = System.currentTimeMillis(),
        targetEndDate = System.currentTimeMillis(),
        actualEndDate = null,
        investment = 220_000.0,
        financialReturn = 300_000.0,
        costReduction = 110_000.0,
        productivityGainPct = 9.0,
        profit = 80_000.0,
        roi = 68.0,
        progressPct = 42,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
)

private fun projectStatusTag(status: ProjectStatus): String = when (status) {
    ProjectStatus.ON_TRACK -> "Execução"
    ProjectStatus.AT_RISK -> "At Risk"
    ProjectStatus.DELAYED -> "Atrasado"
    ProjectStatus.COMPLETED -> "Completo"
    ProjectStatus.CANCELED -> "Cancelado"
}

private fun projectStatusBadge(status: ProjectStatus): BadgeVariant = when (status) {
    ProjectStatus.ON_TRACK -> BadgeVariant.INFO
    ProjectStatus.AT_RISK -> BadgeVariant.WARNING
    ProjectStatus.DELAYED -> BadgeVariant.DANGER
    ProjectStatus.COMPLETED -> BadgeVariant.SUCCESS
    ProjectStatus.CANCELED -> BadgeVariant.GRAY
}

private fun stageTimeLabel(stage: ProjectStage): String = when (stage) {
    ProjectStage.DISCOVERY -> "Fase inicial"
    ProjectStage.PLANNING -> "Prazo ajustado"
    ProjectStage.EXECUTION -> "3 meses restantes"
    ProjectStage.VALIDATION -> "Validação"
    ProjectStage.CLOSED -> "Encerrado"
}

private fun compactMoney(value: Double): String {
    val abs = kotlin.math.abs(value)
    return when {
        abs >= 1_000_000 -> {
            val num = DecimalFormat("0.0").format(value / 1_000_000)
            "R$ ${num}M"
        }
        abs >= 1_000 -> {
            val num = DecimalFormat("0").format(value / 1_000)
            "R$ ${num}k"
        }
        else -> "R$ ${DecimalFormat("0").format(value)}"
    }
}

private fun formatPercent(value: Double): String = DecimalFormat("0").format(value) + "%"
