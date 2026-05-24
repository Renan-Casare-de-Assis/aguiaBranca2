package com.aguiabranca.inovacao.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Danger500
import com.aguiabranca.inovacao.ui.theme.Gray50
import com.aguiabranca.inovacao.ui.theme.Gray400
import com.aguiabranca.inovacao.ui.theme.ProfilePrimaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSecondaryText
import com.aguiabranca.inovacao.ui.theme.ProfileSurfaceBorder
import com.aguiabranca.inovacao.ui.theme.White

@Composable
fun ProjectFormScreen(
    ideaId: String?,
    onBack: () -> Unit,
    viewModel: ProjectFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("") }

    val now = System.currentTimeMillis()
    val sixMonths = now + (180L * 24 * 60 * 60 * 1000)

    LaunchedEffect(uiState.success) {
        if (uiState.success) onBack()
    }

    Scaffold(
        containerColor = Color(0xFFF4F6FA),
        topBar = {
            AguiaTopBar(
                title = if (ideaId != null) "Projeto da Ideia" else "Novo Projeto",
                subtitle = "Formulario objetivo para execucao e retorno",
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
                .padding(innerPadding)
                .background(Color(0xFFF4F6FA)),
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
                        if (ideaId != null) {
                            Text(
                                text = "Criando projeto a partir de uma ideia aprovada.",
                                fontSize = 12.sp,
                                color = ProfileSecondaryText
                            )
                        }

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Titulo do Projeto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = objective,
                            onValueChange = { objective = it },
                            placeholder = { Text("Objetivo") },
                            minLines = 4,
                            maxLines = 6,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = inputColors()
                        )

                        OutlinedTextField(
                            value = investment,
                            onValueChange = { investment = it },
                            placeholder = { Text("Investimento (R$)") },
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

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                viewModel.submit(
                                    ideaId = ideaId,
                                    title = title,
                                    objective = objective,
                                    investment = investment,
                                    startDate = now,
                                    targetEndDate = sixMonths
                                )
                            },
                            enabled = !uiState.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand700)
                        ) {
                            Text(
                                text = if (uiState.isLoading) "Salvando..." else "Criar Projeto",
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
