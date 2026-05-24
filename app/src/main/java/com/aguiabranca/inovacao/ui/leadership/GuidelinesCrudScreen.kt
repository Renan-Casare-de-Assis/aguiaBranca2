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
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.aguiabranca.inovacao.domain.model.Guideline
import com.aguiabranca.inovacao.domain.model.GuidelineStatus
import com.aguiabranca.inovacao.ui.components.AguiaBadge
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.components.BadgeVariant
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Danger500
import com.aguiabranca.inovacao.ui.theme.Gray50
import com.aguiabranca.inovacao.ui.theme.Gray100
import com.aguiabranca.inovacao.ui.theme.Gray300
import com.aguiabranca.inovacao.ui.theme.Gray400
import com.aguiabranca.inovacao.ui.theme.Gray500
import com.aguiabranca.inovacao.ui.theme.Gray600
import com.aguiabranca.inovacao.ui.theme.ProfilePrimaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSecondaryText
import com.aguiabranca.inovacao.ui.theme.White

@Composable
fun GuidelinesCrudScreen(
    onBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = onBack,
    onNavigateToProfile: () -> Unit = {},
    viewModel: GuidelinesCrudViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by rememberSaveable { mutableIntStateOf(1) }
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("Estratégia") }
    var editingId by rememberSaveable { mutableStateOf<String?>(null) }

    val visibleGuidelines = if (uiState.guidelines.isEmpty()) previewGuidelines() else uiState.guidelines

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            editingId = null
            title = ""
            description = ""
            category = "Estratégia"
            viewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Orientações",
                subtitle = "Direcionamento estratégico do ciclo de inovação",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = White
                        )
                    }
                },
                userInitials = "CB"
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
                NewGuidelineCard(
                    title = title,
                    description = description,
                    category = category,
                    onTitleChange = { title = it },
                    onDescriptionChange = { value ->
                        description = if (value.length <= 400) value else value.take(400)
                    },
                    onCategoryChange = { category = it },
                    isLoading = uiState.isLoading,
                    isEditing = editingId != null,
                    onSubmit = {
                        if (title.isBlank()) return@NewGuidelineCard
                        val id = editingId
                        if (id == null) {
                            viewModel.create(title, description, category)
                        } else {
                            viewModel.update(id, title, description, category)
                        }
                    }
                )
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = null,
                        tint = Gray500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ativas", color = Gray500, fontSize = 13.sp)
                }
            }

            if (uiState.isLoading && uiState.guidelines.isEmpty()) {
                item {
                    CenterInfoCard {
                        CircularProgressIndicator(color = Brand700, modifier = Modifier.size(30.dp))
                    }
                }
            }

            items(visibleGuidelines, key = { it.id }) { guideline ->
                GuidelineCrudCard(
                    guideline = guideline,
                    isPreview = uiState.guidelines.isEmpty(),
                    onEdit = {
                        editingId = guideline.id
                        title = guideline.title
                        description = guideline.description
                        category = guideline.pillar
                    },
                    onDelete = {
                        if (!uiState.guidelines.isEmpty()) {
                            viewModel.delete(guideline.id)
                        }
                    }
                )
            }

            if (uiState.guidelines.isEmpty() && !uiState.isLoading) {
                item {
                    Text(
                        text = "Lista em modo visual. Publique uma orientação para iniciar o CRUD real.",
                        color = ProfileSecondaryText,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (uiState.error != null) {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(uiState.error ?: "Erro ao processar orientação.")
            }
        }
    }
}

@Composable
private fun NewGuidelineCard(
    title: String,
    description: String,
    category: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    isLoading: Boolean,
    isEditing: Boolean,
    onSubmit: () -> Unit
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = ProfilePrimaryText, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Nova Orientação", color = ProfilePrimaryText, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
                AguiaBadge(text = "CRUD", variant = BadgeVariant.INFO)
            }

            LabelRow(icon = Icons.Default.Title, label = "Título")
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Título da orientação estratégica") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = inputColors()
            )

            LabelRow(icon = Icons.Default.Description, label = "Descrição")
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Descreva a diretriz para as equipes...") },
                minLines = 4,
                maxLines = 6,
                shape = RoundedCornerShape(10.dp),
                colors = inputColors()
            )

            Text(
                text = "${description.length}/400",
                color = Gray500,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = onCategoryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pilar") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = inputColors()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onSubmit,
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isLoading) "Publicando..." else if (isEditing) "Salvar" else "Publicar",
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .width(52.dp)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Gray50)
                ) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Gray600)
                }
            }
        }
    }
}

@Composable
private fun GuidelineCrudCard(
    guideline: Guideline,
    isPreview: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                    text = guideline.title,
                    color = ProfilePrimaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                AguiaBadge(
                    text = if (guideline.status == GuidelineStatus.ACTIVE) "Ativa" else "Revisão",
                    variant = if (guideline.status == GuidelineStatus.ACTIVE) BadgeVariant.SUCCESS else BadgeVariant.WARNING
                )
            }

            Text(
                text = "Pilar: ${guideline.pillar}",
                color = Gray500,
                fontSize = 12.sp
            )

            Text(
                text = guideline.description,
                color = ProfileSecondaryText,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Gray100)
                ) {
                    Icon(Icons.Default.Create, contentDescription = null, modifier = Modifier.size(14.dp), tint = ProfilePrimaryText)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar", color = ProfilePrimaryText)
                }
                Button(
                    onClick = onDelete,
                    enabled = !isPreview,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Danger500)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Excluir")
                }
            }
        }
    }
}

@Composable
private fun LabelRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Gray600, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = ProfilePrimaryText, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Gray50,
    unfocusedContainerColor = Gray50,
    focusedBorderColor = Gray300,
    unfocusedBorderColor = Gray300,
    focusedTextColor = ProfilePrimaryText,
    unfocusedTextColor = ProfilePrimaryText,
    focusedPlaceholderColor = Gray400,
    unfocusedPlaceholderColor = Gray400,
    cursorColor = Brand700
)

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

private fun previewGuidelines(): List<Guideline> = listOf(
    Guideline(
        id = "preview_guideline_1",
        title = "Priorizar digitalizacao de patio",
        description = "Diretriz para digitalizar checklist e reduzir tempo de conferencia.",
        pillar = "Gestão de Ideias",
        validFrom = null,
        validTo = null,
        status = GuidelineStatus.ACTIVE,
        createdBy = "lead",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    ),
    Guideline(
        id = "preview_guideline_2",
        title = "Reduzir custo logistico em 8%",
        description = "Foco em projetos com retorno rapido e otimização de rotas.",
        pillar = "Mensuração",
        validFrom = null,
        validTo = null,
        status = GuidelineStatus.INACTIVE,
        createdBy = "lead",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
)
