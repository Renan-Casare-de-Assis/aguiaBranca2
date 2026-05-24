package com.aguiabranca.inovacao.ui.manager

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.Button
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
import com.aguiabranca.inovacao.ui.theme.Success100
import com.aguiabranca.inovacao.ui.theme.Success500
import com.aguiabranca.inovacao.ui.theme.Warning500
import com.aguiabranca.inovacao.ui.theme.White
import java.text.DecimalFormat

private data class ProjectPreview(
    val id: String,
    val title: String,
    val status: String,
    val progress: Int,
    val invested: String,
    val roiLabel: String,
    val progressColor: Color
)

@Composable
fun ProjectsScreen(
    onNavigateToProjectForm: () -> Unit,
    onBack: () -> Unit,
    onNavigateToCuration: () -> Unit = onBack,
    onNavigateToProfile: () -> Unit = {},
    viewModel: ProjectsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(1) }

    val previewProjects = remember { defaultProjectPreviews() }
    val showProjects = if (uiState.projects.isEmpty()) previewProjects else emptyList()

    val investedTotal = if (uiState.projects.isEmpty()) 430000.0 else uiState.projects.sumOf { it.investment }
    val returnTotal = if (uiState.projects.isEmpty()) 762000.0 else uiState.projects.sumOf { it.financialReturn }
    val roiPct = if (investedTotal > 0.0) (returnTotal / investedTotal) * 100.0 else 0.0

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Projetos",
                subtitle = "Acompanhamento de execucao e retorno",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = White
                        )
                    }
                },
                notificationCount = 0,
                userInitials = ""
            )
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Curadoria", "Projetos", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> onNavigateToCuration()
                        1 -> Unit
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = Gray500,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Meus Projetos",
                            color = Gray500,
                            fontSize = 13.sp
                        )
                    }
                    AguiaBadge(text = "On track", variant = BadgeVariant.SUCCESS)
                }
            }

            when {
                uiState.isLoading -> {
                    item {
                        CenterInfoCard {
                            CircularProgressIndicator(color = Brand700, modifier = Modifier.size(30.dp))
                        }
                    }
                }

                uiState.error != null -> {
                    item {
                        CenterInfoCard {
                            Text(uiState.error ?: "Erro ao carregar projetos.", color = Danger500, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { viewModel.load() }) {
                                Text("Tentar novamente", color = Brand700)
                            }
                        }
                    }
                }

                uiState.projects.isEmpty() -> {
                    items(showProjects, key = { it.id }) { preview ->
                        ProjectPreviewCard(preview = preview)
                    }
                }

                else -> {
                    items(uiState.projects, key = { it.id }) { project ->
                        ProjectCard(project = project)
                    }
                }
            }

            item {
                Button(
                    onClick = onNavigateToProjectForm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Novo Projeto", fontWeight = FontWeight.Bold)
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Paid,
                        contentDescription = null,
                        tint = Gray500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Resumo Financeiro", color = Gray500, fontSize = 13.sp)
                }
            }

            item {
                FinancialSummaryCard(
                    invested = investedTotal,
                    returned = returnTotal,
                    roiPct = roiPct
                )
            }
        }
    }
}

@Composable
private fun ProjectCard(project: Project) {
    val progress = project.progressPct.coerceIn(0, 100)
    val progressColor = when (project.status) {
        ProjectStatus.ON_TRACK -> Success500
        ProjectStatus.AT_RISK -> Warning500
        ProjectStatus.DELAYED -> Danger500
        else -> Brand700
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
                AguiaBadge(text = project.status.displayName(), variant = badgeVariant(project.status))
            }

            Text(text = project.objective, color = ProfileSecondaryText, fontSize = 13.sp, maxLines = 2)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progresso", color = Gray600, fontSize = 12.sp)
                Text("$progress%", color = Gray600, fontSize = 12.sp)
            }

            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = progressColor,
                trackColor = Gray300
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Paid, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(currency(project.investment) + " investido", color = Gray500, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(project.stage.displayName(), color = Gray500, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ProjectPreviewCard(preview: ProjectPreview) {
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
                    text = preview.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = ProfilePrimaryText,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                AguiaBadge(
                    text = preview.status,
                    variant = if (preview.status.contains("On Track", ignoreCase = true)) {
                        BadgeVariant.SUCCESS
                    } else {
                        BadgeVariant.WARNING
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progresso", color = Gray600, fontSize = 12.sp)
                Text("${preview.progress}%", color = Gray600, fontSize = 12.sp)
            }

            LinearProgressIndicator(
                progress = { preview.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = preview.progressColor,
                trackColor = Gray300
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Paid, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(preview.invested, color = Gray500, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Gray500, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(preview.roiLabel, color = Gray500, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun FinancialSummaryCard(
    invested: Double,
    returned: Double,
    roiPct: Double
) {
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
                Text("Total investido", color = Gray600, fontSize = 13.sp)
                AguiaBadge(text = "ROI ${formatPercent(roiPct)}", variant = BadgeVariant.SUCCESS)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    title = "INVESTIDO",
                    value = currency(invested),
                    containerColor = Color(0xFF243B67),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "RETORNO",
                    value = currency(returned),
                    containerColor = Color(0xFF32B259),
                    modifier = Modifier.weight(1f)
                )
            }

            MiniBarsChart()
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, color = White.copy(alpha = 0.85f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            Text(value, color = White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MiniBarsChart() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Gray100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            listOf(38.dp, 56.dp, 72.dp, 48.dp, 80.dp).forEach { h ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(h)
                        .background(Color(0xFF526A94), RoundedCornerShape(4.dp))
                )
            }
        }
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

private fun badgeVariant(status: ProjectStatus): BadgeVariant = when (status) {
    ProjectStatus.ON_TRACK -> BadgeVariant.SUCCESS
    ProjectStatus.AT_RISK -> BadgeVariant.WARNING
    ProjectStatus.DELAYED -> BadgeVariant.DANGER
    ProjectStatus.COMPLETED -> BadgeVariant.INFO
    ProjectStatus.CANCELED -> BadgeVariant.GRAY
}

private fun formatPercent(value: Double): String = DecimalFormat("#,##0").format(value) + "%"

private fun currency(value: Double): String {
    val formatter = DecimalFormat("#,##0")
    return "R$ ${formatter.format(value / 1000)}k"
}

private fun defaultProjectPreviews(): List<ProjectPreview> = listOf(
    ProjectPreview(
        id = "project_preview_1",
        title = "Automacao de triagem de embarque",
        status = "On Track",
        progress = 65,
        invested = "R$ 350k investido",
        roiLabel = "ROI 77%",
        progressColor = Success500
    ),
    ProjectPreview(
        id = "project_preview_2",
        title = "Rastreamento de cargas",
        status = "Planejamento",
        progress = 15,
        invested = "R$ 80k investido",
        roiLabel = "Inicio: Jun/26",
        progressColor = Warning500
    )
)

