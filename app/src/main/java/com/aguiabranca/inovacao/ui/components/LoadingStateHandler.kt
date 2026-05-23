package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aguiabranca.inovacao.ui.theme.*

@Composable
fun LoadingStateHandler(
    isLoading: Boolean,
    error: String? = null,
    isEmpty: Boolean = false,
    emptyMessage: String = "Nenhum item encontrado.",
    onRetry: () -> Unit = {},
    content: @Composable () -> Unit
) {
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Brand700)
            }
        }
        error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = error,
                    color = Danger600,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                AguiaButton(
                    text = "Tentar novamente",
                    onClick = onRetry,
                    variant = ButtonVariant.PRIMARY
                )
            }
        }
        isEmpty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyMessage,
                    color = Gray500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
        else -> content()
    }
}

