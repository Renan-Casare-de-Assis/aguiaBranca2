package com.aguiabranca.inovacao.ui.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.ProjectStage
import com.aguiabranca.inovacao.domain.model.ProjectStatus
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Danger500
import com.aguiabranca.inovacao.ui.theme.Gray50
import com.aguiabranca.inovacao.ui.theme.Gray400
import com.aguiabranca.inovacao.ui.theme.ProfilePrimaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSurfaceBorder
import com.aguiabranca.inovacao.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectEditScreen(
    projectId: String,
    onBack: () -> Unit,
    viewModel: ProjectEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("0") }
    var financialReturn by remember { mutableStateOf("0") }
    var costReduction by remember { mutableStateOf("0") }
    var productivityGainPct by remember { mutableStateOf("0") }
    var progressPct by remember { mutableStateOf("0") }
    var stage by remember { mutableStateOf(ProjectStage.DISCOVERY) }
    var status by remember { mutableStateOf(ProjectStatus.ON_TRACK) }
    var stageExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    LaunchedEffect(uiState.project) {
        uiState.project?.let { project ->
            title = project.title
            objective = project.objective
            investment = project.investment.toString()
            financialReturn = project.financialReturn.toString()
            costReduction = project.costReduction.toString()
            productivityGainPct = project.productivityGainPct.toString()
            progressPct = project.progressPct.toString()
            stage = project.stage
            status = project.status
        }
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) onBack()
    }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Editar Projeto",
                subtitle = "Atualize os dados e salve no Oracle",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = White
                        )
                    }
                },
                userInitials = ""
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
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
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Título do projeto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = objective,
                            onValueChange = { objective = it },
                            placeholder = { Text("Objetivo") },
                            minLines = 3,
                            maxLines = 5,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = stage.displayName(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Etapa") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { stageExpanded = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )
                        DropdownMenu(
                            expanded = stageExpanded,
                            onDismissRequest = { stageExpanded = false }
                        ) {
                            ProjectStage.entries.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.displayName()) },
                                    onClick = {
                                        stage = item
                                        stageExpanded = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = status.displayName(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { statusExpanded = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )
                        DropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            ProjectStatus.entries.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.displayName()) },
                                    onClick = {
                                        status = item
                                        statusExpanded = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = investment,
                            onValueChange = { investment = it },
                            placeholder = { Text("Investimento") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = financialReturn,
                            onValueChange = { financialReturn = it },
                            placeholder = { Text("Retorno financeiro") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = costReduction,
                            onValueChange = { costReduction = it },
                            placeholder = { Text("Redução de custo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = productivityGainPct,
                            onValueChange = { productivityGainPct = it },
                            placeholder = { Text("Ganho de produtividade (%)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = progressPct,
                            onValueChange = { progressPct = it },
                            placeholder = { Text("Progresso (%)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        if (uiState.error != null) {
                            Text(
                                text = uiState.error ?: "Erro ao salvar projeto.",
                                color = Danger500,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Button(
                            onClick = {
                                viewModel.updateProject(
                                    id = projectId,
                                    title = title,
                                    objective = objective,
                                    stage = stage,
                                    status = status,
                                    investment = investment.toDoubleOrNull() ?: 0.0,
                                    financialReturn = financialReturn.toDoubleOrNull() ?: 0.0,
                                    costReduction = costReduction.toDoubleOrNull() ?: 0.0,
                                    productivityGainPct = productivityGainPct.toDoubleOrNull() ?: 0.0,
                                    progressPct = progressPct.toIntOrNull() ?: 0
                                )
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                        ) {
                            Text(
                                text = if (uiState.isSaving) "Salvando..." else "Salvar Alterações",
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Gray50,
    unfocusedContainerColor = Gray50,
    focusedBorderColor = ProfileSurfaceBorder,
    unfocusedBorderColor = ProfileSurfaceBorder,
    focusedTextColor = ProfilePrimaryText,
    unfocusedTextColor = ProfilePrimaryText,
    focusedPlaceholderColor = Gray400,
    unfocusedPlaceholderColor = Gray400,
    cursorColor = Brand700
)

