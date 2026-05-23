package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.White

@Composable
fun AguiaAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val fontSize = when {
        size <= 32.dp -> 11.sp
        size <= 44.dp -> 14.sp
        size <= 56.dp -> 16.sp
        else          -> 20.sp
    }

    val displayInitials = initials
        .trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .take(2)
        .ifEmpty { "?" }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brand700),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayInitials,
            color = White,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}

