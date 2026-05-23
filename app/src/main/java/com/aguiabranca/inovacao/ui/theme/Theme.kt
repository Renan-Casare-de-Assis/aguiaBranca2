package com.aguiabranca.inovacao.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AguiaColorScheme = lightColorScheme(
    primary            = Brand700,
    onPrimary          = White,
    primaryContainer   = Brand100,
    onPrimaryContainer = Brand900,
    secondary          = Gold500,
    onSecondary        = White,
    secondaryContainer = Gold100,
    onSecondaryContainer = Gold600,
    background         = Gray50,
    onBackground       = Gray900,
    surface            = White,
    onSurface          = Gray900,
    surfaceVariant     = Gray100,
    onSurfaceVariant   = Gray600,
    outline            = Gray300,
    error              = Danger500,
    onError            = White,
    errorContainer     = Danger100,
    onErrorContainer   = Danger600
)

@Composable
fun AguiaBranca2Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AguiaColorScheme,
        typography  = AguiaTypography,
        shapes      = AguiaShapes,
        content     = content
    )
}