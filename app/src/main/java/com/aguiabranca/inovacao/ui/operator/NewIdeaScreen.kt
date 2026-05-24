package com.aguiabranca.inovacao.ui.operator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.IdeaCategory
import com.aguiabranca.inovacao.ui.components.AguiaBottomNav
import com.aguiabranca.inovacao.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIdeaScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit = onBack,
    onNavigateToIdeas: () -> Unit = onBack,
    onNavigateToProfile: () -> Unit = {},
    viewModel: NewIdeaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(IdeaCategory.PROCESS) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(1) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) onBack()
    }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            NewIdeaHeader(onBack = onBack)
        },
        bottomBar = {
            AguiaBottomNav(
                items = listOf("Início", "Ideias", "Perfil"),
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToIdeas()
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FormCard(
                    title = title,
                    onTitleChange = { title = it },
                    description = description,
                    onDescriptionChange = { value ->
                        description = if (value.length <= 500) value else value.take(500)
                    },
                    selectedCategory = selectedCategory,
                    categoryExpanded = categoryExpanded,
                    onCategoryExpandedChange = { categoryExpanded = it },
                    onCategorySelected = {
                        selectedCategory = it
                        categoryExpanded = false
                    },
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onSubmit = { viewModel.submit(title, description, selectedCategory) }
                )
            }

            item {
                TipsCard()
            }
        }
    }
}

@Composable
private fun NewIdeaHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Brand900, Brand700)
                )
            )
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Nova Ideia",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                }
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = "2 min",
                            color = White,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = White.copy(alpha = 0.12f)
                    )
                )
            }
            Text(
                text = "Registro rápido para melhoria operacional",
                color = White.copy(alpha = 0.78f),
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormCard(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedCategory: IdeaCategory,
    categoryExpanded: Boolean,
    onCategoryExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (IdeaCategory) -> Unit,
    isLoading: Boolean,
    error: String?,
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
            LabelWithIcon(icon = Icons.Default.Title, text = "Título da ideia")
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = { Text("Checklist digital para conferência") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = inputColors()
            )

            Text(
                text = "Seja objetivo. Ex: \"Checklist digital de embarque\"",
                color = ProfileSecondaryText,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )

            LabelWithIcon(icon = Icons.Default.Category, text = "Categoria")
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = onCategoryExpandedChange
            ) {
                OutlinedTextField(
                    value = selectedCategory.displayName(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = inputColors()
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { onCategoryExpandedChange(false) }
                ) {
                    IdeaCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.displayName()) },
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }

            LabelWithIcon(icon = Icons.Default.Description, text = "Descrição")
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = { Text("Criar checklist no celular para reduzir erros de conferência...") },
                minLines = 5,
                maxLines = 7,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = inputColors()
            )

            Text(
                text = "${description.length}/500",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                color = ProfileSecondaryText,
                fontSize = 12.sp
            )

            Text(
                text = "Inclua: problema atual, ganho esperado e área impactada.",
                color = ProfileSecondaryText,
                fontSize = 12.sp
            )

            if (error != null) {
                Text(
                    text = error,
                    color = Danger600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

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
                        imageVector = Icons.Default.NorthEast,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isLoading) "Enviando..." else "Enviar Ideia",
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .size(width = 52.dp, height = 46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Gray50),
                    border = BorderStroke(1.dp, ProfileSurfaceBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Salvar rascunho",
                        tint = Gray600
                    )
                }
            }
        }
    }
}

@Composable
private fun TipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dicas para Aprovação",
                    fontWeight = FontWeight.SemiBold,
                    color = ProfilePrimaryText,
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Success100)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = Success600,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Dica", color = Success600, fontSize = 11.sp)
                    }
                }
            }

            TipLine(text = "Descreva o problema atual claramente")
            TipLine(text = "Estime o ganho esperado (tempo, custo)")
            TipLine(text = "Indique a área impactada")
        }
    }
}

@Composable
private fun TipLine(text: String) {
    Text(
        text = "•  $text",
        color = ProfileSecondaryText,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
}

@Composable
private fun LabelWithIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Gray600,
            modifier = Modifier.size(14.dp)
        )
        Text(text = text, color = ProfilePrimaryText, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ProfileSurfaceBorder,
    unfocusedBorderColor = ProfileSurfaceBorder,
    focusedContainerColor = Gray50,
    unfocusedContainerColor = Gray50,
    focusedTextColor = ProfilePrimaryText,
    unfocusedTextColor = ProfilePrimaryText,
    focusedPlaceholderColor = Gray400,
    unfocusedPlaceholderColor = Gray400,
    cursorColor = Brand700
)
