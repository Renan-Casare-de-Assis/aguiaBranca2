package com.aguiabranca.inovacao.ui.operator

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.domain.model.IdeaPriority
import com.aguiabranca.inovacao.domain.model.IdeaStatus
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
fun IdeaEditScreen(
    ideaId: String,
    onBack: () -> Unit,
    viewModel: IdeaEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var comment by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(IdeaStatus.NEW) }
    var priority by remember { mutableStateOf(IdeaPriority.MEDIUM) }
    var statusExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(ideaId) {
        viewModel.loadIdea(ideaId)
    }

    LaunchedEffect(uiState.idea) {
        uiState.idea?.let { idea ->
            comment = idea.managerComment.orEmpty()
            status = idea.status
            priority = idea.priority ?: IdeaPriority.MEDIUM
        }
    }

    LaunchedEffect(uiState.success) {
        if (uiState.success) onBack()
    }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = "Editar Ideia",
                subtitle = "Atualize status, prioridade e comentário",
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
                            value = status.name,
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
                            IdeaStatus.entries.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.name) },
                                    onClick = {
                                        status = item
                                        statusExpanded = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = priority.displayName(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Prioridade") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { priorityExpanded = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )
                        DropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false }
                        ) {
                            IdeaPriority.entries.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item.displayName()) },
                                    onClick = {
                                        priority = item
                                        priorityExpanded = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            placeholder = { Text("Comentário da curadoria") },
                            minLines = 4,
                            maxLines = 6,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        if (uiState.error != null) {
                            Text(
                                text = uiState.error ?: "Erro ao atualizar ideia.",
                                color = Danger500,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Button(
                            onClick = {
                                viewModel.updateStatus(ideaId, status, comment.ifBlank { null })
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                        ) {
                            Text(
                                text = if (uiState.isSaving) "Salvando..." else "Salvar Status",
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.updatePriority(ideaId, priority)
                            },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                        ) {
                            Text(
                                text = "Salvar Prioridade",
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.deleteIdea(ideaId) },
                            enabled = !uiState.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Danger500)
                        ) {
                            Text(
                                text = "Excluir Ideia",
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

