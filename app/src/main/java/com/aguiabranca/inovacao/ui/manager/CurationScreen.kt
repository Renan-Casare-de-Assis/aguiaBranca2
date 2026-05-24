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
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.ui.components.AguiaBadge
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.BadgeVariant
import com.aguiabranca.inovacao.ui.components.StatusChip
import com.aguiabranca.inovacao.ui.theme.Brand100
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
import com.aguiabranca.inovacao.ui.theme.Warning100
import com.aguiabranca.inovacao.ui.theme.Warning600
import com.aguiabranca.inovacao.ui.theme.White

private enum class CurationFilter {
    PENDING,
    HIGH,
    MEDIUM
}

private data class PreviewIdea(
    val id: String,
    val title: String,
    val operator: String,
    val statusText: String,
    val priorityLabel: String,
    val description: String,
    val showApproveActions: Boolean
)

@Composable
fun CurationScreen(
    onNavigateToProjectForm: (String?) -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToGuidelines: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: CurationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf(CurationFilter.PENDING) }

    val filteredIdeas = remember(uiState.ideas, searchQuery, selectedFilter) {
        uiState.ideas.filter { idea ->
            val matchesSearch = searchQuery.isBlank() ||
                idea.title.contains(searchQuery, ignoreCase = true) ||
                idea.operatorName.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                CurationFilter.PENDING -> true
                CurationFilter.HIGH -> idea.priority == IdeaPriority.HIGH
                CurationFilter.MEDIUM -> idea.priority == IdeaPriority.MEDIUM
            }

            matchesSearch && matchesFilter
        }
    }

    val highCount = uiState.ideas.count { it.priority == IdeaPriority.HIGH }
    val mediumCount = uiState.ideas.count { it.priority == IdeaPriority.MEDIUM }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Curadoria",
                subtitle = "Priorização e aprovação de ideias",
                notificationCount = 7,
                userInitials = "MG"
            )
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Curadoria", "Projetos", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> Unit
                        1 -> onNavigateToProjects()
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
                SearchField(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            item {
                FilterRow(
                    selectedFilter = selectedFilter,
                    pendingCount = uiState.ideas.size,
                    highCount = highCount,
                    mediumCount = mediumCount,
                    onSelect = { selectedFilter = it }
                )
            }

            item {
                OrientationCard(onClick = onNavigateToGuidelines)
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
                            Text(
                                text = uiState.error ?: "Erro ao carregar ideias.",
                                color = Danger500,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { viewModel.load() }) {
                                Text("Tentar novamente", color = Brand700)
                            }
                        }
                    }
                }

                uiState.ideas.isEmpty() -> {
                    items(previewIdeas()) { preview ->
                        PreviewIdeaCard(preview = preview, onCreateProject = { onNavigateToProjectForm(null) })
                    }
                    item { EmptyQueueNotice() }
                }

                filteredIdeas.isEmpty() -> {
                    item { EmptySearchState() }
                }

                else -> {
                    items(filteredIdeas, key = { it.id }) { idea ->
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

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Buscar ideia ou autor...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = Gray500)
        },
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedBorderColor = Gray300,
            unfocusedBorderColor = Gray300,
            focusedTextColor = ProfilePrimaryText,
            unfocusedTextColor = ProfilePrimaryText,
            focusedPlaceholderColor = Gray400,
            unfocusedPlaceholderColor = Gray400,
            cursorColor = Brand700
        )
    )
}

@Composable
private fun FilterRow(
    selectedFilter: CurationFilter,
    pendingCount: Int,
    highCount: Int,
    mediumCount: Int,
    onSelect: (CurationFilter) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChipItem(
            label = "Pendentes($pendingCount)",
            icon = Icons.Default.FilterAlt,
            selected = selectedFilter == CurationFilter.PENDING,
            onClick = { onSelect(CurationFilter.PENDING) }
        )
        FilterChipItem(
            label = "Alta ${if (highCount > 0) "($highCount)" else ""}".trim(),
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            selected = selectedFilter == CurationFilter.HIGH,
            onClick = { onSelect(CurationFilter.HIGH) }
        )
        FilterChipItem(
            label = "Media ${if (mediumCount > 0) "($mediumCount)" else ""}".trim(),
            icon = Icons.AutoMirrored.Filled.Sort,
            selected = selectedFilter == CurationFilter.MEDIUM,
            onClick = { onSelect(CurationFilter.MEDIUM) }
        )
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) Brand100 else Gray300
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) White else Color.Transparent,
            contentColor = if (selected) Brand700 else Gray600
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun OrientationCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7EDF7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TipsAndUpdates,
                    contentDescription = null,
                    tint = Brand700,
                    modifier = Modifier.size(18.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Orientacao da Semana",
                        color = ProfilePrimaryText,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Priorizar digitalização de pátio - alinhe suas avaliações",
                        color = ProfileSecondaryText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ManageSearch,
                contentDescription = null,
                tint = Gray500,
                modifier = Modifier.size(18.dp)
            )
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
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = idea.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = ProfilePrimaryText,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                PriorityBadge(priority = idea.priority)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonOutline, contentDescription = null, tint = Gray500, modifier = Modifier.size(14.dp))
                Text("${idea.operatorName} • ${statusDisplayText(idea.status)}", color = Gray500, fontSize = 12.sp)
            }

            Text(
                text = idea.description,
                color = ProfileSecondaryText,
                fontSize = 13.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Comentário (opcional)") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Gray100,
                    unfocusedContainerColor = Gray100,
                    focusedBorderColor = Gray300,
                    unfocusedBorderColor = Gray300
                )
            )

            if (idea.priority == IdeaPriority.HIGH || idea.status == IdeaStatus.UNDER_REVIEW) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onApprove(comment) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand700),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Aprovar")
                    }
                    Button(
                        onClick = { onReject(comment) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Danger500),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reprovar")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onPrioritize(IdeaPriority.HIGH) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Prioridade", color = Brand700)
                    }
                    Button(
                        onClick = onCreateProject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand700),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Vincular")
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewIdeaCard(
    preview: PreviewIdea,
    onCreateProject: () -> Unit
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
                    text = preview.priorityLabel,
                    variant = if (preview.priorityLabel.contains("Alta")) BadgeVariant.DANGER else BadgeVariant.INFO
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonOutline, contentDescription = null, tint = Gray500, modifier = Modifier.size(14.dp))
                Text("${preview.operator} • ${preview.statusText}", color = Gray500, fontSize = 12.sp)
            }

            Text(
                text = preview.description,
                color = ProfileSecondaryText,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (preview.showApproveActions) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand700),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Aprovar")
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Danger500),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reprovar")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Prioridade", color = Brand700)
                    }
                    Button(
                        onClick = onCreateProject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand700),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Vincular")
                    }
                }
            }
        }
    }
}

@Composable
private fun PriorityBadge(priority: IdeaPriority?) {
    when (priority) {
        IdeaPriority.HIGH -> AguiaBadge(text = "Alta", variant = BadgeVariant.DANGER)
        IdeaPriority.MEDIUM -> AguiaBadge(text = "Media", variant = BadgeVariant.INFO)
        IdeaPriority.LOW -> AguiaBadge(text = "Baixa", variant = BadgeVariant.GRAY)
        null -> AguiaBadge(text = "Sem prioridade", variant = BadgeVariant.GRAY)
    }
}

@Composable
private fun EmptySearchState() {
    CenterInfoCard {
        Icon(Icons.Default.Search, contentDescription = null, tint = Gray400, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text("Nenhuma ideia encontrada com esse filtro.", color = ProfileSecondaryText, fontSize = 13.sp)
    }
}

@Composable
private fun EmptyQueueNotice() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(Icons.Outlined.Inbox, contentDescription = null, tint = Gray400, modifier = Modifier.size(24.dp))
        Text(
            text = "Nao ha mais ideias pendentes nesta fila.",
            color = Gray500,
            fontSize = 13.sp
        )
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

private fun previewIdeas(): List<PreviewIdea> = listOf(
    PreviewIdea(
        id = "preview_1",
        title = "QR Code checklist de frota",
        operator = "Carla M.",
        statusText = "Em analise",
        priorityLabel = "Alta",
        description = "Checklist de inspeção antes da saída dos veículos.",
        showApproveActions = true
    ),
    PreviewIdea(
        id = "preview_2",
        title = "Portal unico de ocorrencias",
        operator = "Lucas V.",
        statusText = "Priorizada",
        priorityLabel = "Media",
        description = "Centralizar ocorrencias e reduzir retrabalho entre turnos.",
        showApproveActions = false
    )
)

private fun statusDisplayText(status: IdeaStatus): String = when (status) {
    IdeaStatus.NEW -> "Nova"
    IdeaStatus.UNDER_REVIEW -> "Em analise"
    IdeaStatus.PRIORITIZED -> "Priorizada"
    IdeaStatus.APPROVED -> "Aprovada"
    IdeaStatus.REJECTED -> "Reprovada"
    IdeaStatus.IN_PROJECT -> "Em projeto"
}
