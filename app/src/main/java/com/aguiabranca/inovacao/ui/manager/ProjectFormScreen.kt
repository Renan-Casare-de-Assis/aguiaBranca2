package com.aguiabranca.inovacao.ui.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguiabranca.inovacao.ui.components.AguiaTopBar
import com.aguiabranca.inovacao.ui.theme.Dourado

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
        topBar = {
            AguiaTopBar(
                title = if (ideaId != null) "Projeto da Ideia" else "Novo Projeto",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Dourado)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (ideaId != null) {
                Text(
                    text = "Criando projeto a partir de uma ideia aprovada.",
                    fontSize = 13.sp,
                    color = Dourado
                )
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Projeto") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = objective,
                onValueChange = { objective = it },
                label = { Text("Objetivo") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = investment,
                onValueChange = { investment = it },
                label = { Text("Investimento (R$)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                colors = ButtonDefaults.buttonColors(containerColor = Dourado),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(
                    text = if (uiState.isLoading) "Salvando..." else "Criar Projeto",
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

