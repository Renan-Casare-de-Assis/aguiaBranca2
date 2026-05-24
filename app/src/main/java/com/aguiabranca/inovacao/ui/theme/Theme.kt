package com.aguiabranca.inovacao.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AguiaDarkColorScheme = darkColorScheme(
    primary        = Dourado,
    onPrimary      = Color.White,
    secondary      = VerdeSuccess,
    background     = AzulMarinho,
    surface        = Color(0xFF1E2A3A),
    onBackground   = Color.White,
    onSurface      = Color.White,
    error          = Color(0xFFCF6679)
)

@Composable
fun AguiaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AguiaDarkColorScheme,
        typography  = AguiaTypography,
        shapes      = AguiaShapes,
        content     = content
    )
}