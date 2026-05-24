package com.aguiabranca.inovacao.ui.operator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.Idea
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaCard
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.StatusChip
import com.aguiabranca.inovacao.ui.theme.Brand300
import com.aguiabranca.inovacao.ui.theme.Brand500
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Danger500
import com.aguiabranca.inovacao.ui.theme.Gray400
import com.aguiabranca.inovacao.ui.theme.Gray500
import com.aguiabranca.inovacao.ui.theme.Gold400
import com.aguiabranca.inovacao.ui.theme.Gold500
import com.aguiabranca.inovacao.ui.theme.ProfileChipBg
import com.aguiabranca.inovacao.ui.theme.ProfileChipText
import com.aguiabranca.inovacao.ui.theme.ProfilePrimaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSecondaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSurfaceBorder
import com.aguiabranca.inovacao.ui.theme.Success500
import com.aguiabranca.inovacao.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

private enum class IdeaFilter {
    ALL,
    APPROVED,
    UNDER_REVIEW
}

@Composable
fun MyIdeasScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit = onBack,
    onNavigateToNewIdea: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: MyIdeasViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf(IdeaFilter.ALL) }
    var selectedTab by rememberSaveable { mutableIntStateOf(1) }

    val initials = uiState.user?.name
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.take(2)
        ?.joinToString("") { it.first().uppercaseChar().toString() }
        .orEmpty()

    val approvedCount = uiState.ideas.count { it.status.name.equals("APPROVED", ignoreCase = true) }
    val underReviewCount = uiState.ideas.count { it.status.name.equals("UNDER_REVIEW", ignoreCase = true) }

    val filteredIdeas = remember(uiState.ideas, searchQuery, selectedFilter) {
        uiState.ideas.filter { idea ->
            val matchesSearch = searchQuery.isBlank() ||
                idea.title.contains(searchQuery, ignoreCase = true) ||
                idea.description.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                IdeaFilter.ALL -> true
                IdeaFilter.APPROVED -> idea.status.name.equals("APPROVED", ignoreCase = true)
                IdeaFilter.UNDER_REVIEW -> idea.status.name.equals("UNDER_REVIEW", ignoreCase = true)
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Minhas Ideias",
                subtitle = "Acompanhe status e retorno da curadoria",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Gold400
                        )
                    }
                },
                notificationCount = 3,
                userInitials = if (initials.isBlank()) "OP" else initials
            )
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Início", "Ideias", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> onNavigateToHome()
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            item {
                FilterChips(
                    selectedFilter = selectedFilter,
                    allCount = uiState.ideas.size,
                    approvedCount = approvedCount,
                    underReviewCount = underReviewCount,
                    onFilterSelected = { selectedFilter = it }
                )
            }

            when {
                uiState.isLoading -> {
                    item { LoadingCard() }
                }

                uiState.error != null -> {
                    item {
                        ErrorCard(
                            message = uiState.error ?: "Erro ao carregar ideias.",
                            onRetry = { viewModel.load() },
                            onCreateIdea = onNavigateToNewIdea
                        )
                    }
                }

                filteredIdeas.isEmpty() -> {
                    item { EmptyStateCard(onCreateIdea = onNavigateToNewIdea) }
                }

                else -> {
                    items(filteredIdeas, key = { it.id }) { idea ->
                        IdeaDetailCard(idea = idea)
                    }
                }
            }

            item {
                SummaryCard(
                    total = uiState.ideas.size,
                    approved = approvedCount,
                    returnText = estimateReturnText(uiState.ideas)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar ideia...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = ProfileSurfaceBorder,
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            cursorColor = Brand700,
            focusedLeadingIconColor = Gray500,
            unfocusedLeadingIconColor = Gray500,
            focusedTextColor = ProfilePrimaryText,
            unfocusedTextColor = ProfilePrimaryText,
            focusedPlaceholderColor = Gray400,
            unfocusedPlaceholderColor = Gray400
        )
    )
}

@Composable
private fun FilterChips(
    selectedFilter: IdeaFilter,
    allCount: Int,
    approvedCount: Int,
    underReviewCount: Int,
    onFilterSelected: (IdeaFilter) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterPill(
            label = "Todas ($allCount)",
            selected = selectedFilter == IdeaFilter.ALL,
            icon = Icons.AutoMirrored.Filled.ListAlt,
            onClick = { onFilterSelected(IdeaFilter.ALL) }
        )
        FilterPill(
            label = "Aprovadas ($approvedCount)",
            selected = selectedFilter == IdeaFilter.APPROVED,
            icon = Icons.Default.CheckCircleOutline,
            onClick = { onFilterSelected(IdeaFilter.APPROVED) }
        )
        FilterPill(
            label = "Análise ($underReviewCount)",
            selected = selectedFilter == IdeaFilter.UNDER_REVIEW,
            icon = Icons.Default.Tune,
            onClick = { onFilterSelected(IdeaFilter.UNDER_REVIEW) }
        )
    }
}

@Composable
private fun FilterPill(
    label: String,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = if (selected) Brand700 else Gray500
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) ProfilePrimaryText else ProfileSecondaryText
        )
    }
}

@Composable
private fun LoadingCard() {
    AguiaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = Brand700, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onRetry: () -> Unit,
    onCreateIdea: () -> Unit
) {
    AguiaCard(accentColor = Danger500) {
        Text(
            text = "Não foi possível carregar suas ideias",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = ProfilePrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            fontSize = 13.sp,
            color = ProfileSecondaryText
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TextButton(onClick = onRetry) { Text("Tentar novamente", color = Brand700) }
            TextButton(onClick = onCreateIdea) { Text("Nova ideia", color = Brand700) }
        }
    }
}

@Composable
private fun EmptyStateCard(onCreateIdea: () -> Unit) {
    AguiaCard(accentColor = Brand300) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Brand700.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkOutline,
                    contentDescription = null,
                    tint = ProfileChipText
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Você ainda não enviou nenhuma ideia.",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = ProfilePrimaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Use o botão de nova ideia para registrar sua sugestão e acompanhar o retorno da curadoria.",
                    fontSize = 13.sp,
                    color = ProfileSecondaryText,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onCreateIdea,
                    colors = ButtonDefaults.buttonColors(containerColor = Brand700),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Nova ideia", color = White)
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    total: Int,
    approved: Int,
    returnText: String
) {
    AguiaCard {
        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCell(
                value = total.toString(),
                label = "ENVIADAS",
                modifier = Modifier.weight(1f)
            )
            DividerVertical(height = 56.dp)
            SummaryCell(
                value = approved.toString(),
                label = "APROVADAS",
                modifier = Modifier.weight(1f)
            )
            DividerVertical(height = 56.dp)
            SummaryCell(
                value = returnText,
                label = "RETORNO",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryCell(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 6.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Brand700
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp,
            color = ProfileSecondaryText
        )
    }
}

@Composable
private fun IdeaDetailCard(idea: Idea) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }

    AguiaCard(accentColor = statusAccentColor(idea.status.name)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Brand700.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkOutline,
                    contentDescription = null,
                    tint = ProfileChipText,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = idea.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = ProfilePrimaryText,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    StatusChip(status = idea.status.name)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = idea.description,
                    fontSize = 13.sp,
                    color = ProfileSecondaryText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Gray400
                        )
                        Text(
                            text = idea.unit ?: "Operação",
                            fontSize = 11.sp,
                            color = ProfileSecondaryText
                        )
                    }
                    Text(
                        text = dateFormat.format(Date(idea.updatedAt)),
                        fontSize = 11.sp,
                        color = ProfileSecondaryText
                    )
                }

                if (!idea.managerComment.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = ProfileSurfaceBorder)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Feedback da curadoria",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Brand700
                    )
                    Text(
                        text = idea.managerComment,
                        fontSize = 12.sp,
                        color = ProfileSecondaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun estimateReturnText(ideas: List<Idea>): String {
    if (ideas.isEmpty()) return "--"
    val latestUpdate = ideas.maxOf { it.updatedAt }
    val days = max(1, ((System.currentTimeMillis() - latestUpdate) / 86_400_000L).toInt())
    return "${days}d"
}

private fun statusAccentColor(status: String): Color = when (status.uppercase()) {
    "APPROVED", "APROVADA" -> Success500
    "UNDER_REVIEW", "EM_ANALISE" -> Gold500
    "REJECTED", "REJEITADA" -> Danger500
    "PRIORITIZED", "PRIORIZADA" -> Brand500
    else -> Brand300
}

@Composable
private fun DividerVertical(height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(height)
            .background(ProfileSurfaceBorder)
    )
}
