package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aguiabranca.inovacao.ui.theme.*

enum class BadgeVariant {
    SUCCESS, WARNING, DANGER, INFO, GOLD, GRAY
}

@Composable
fun AguiaBadge(
    text: String,
    variant: BadgeVariant = BadgeVariant.INFO,
    modifier: Modifier = Modifier
) {
    val backgroundColor: Color = when (variant) {
        BadgeVariant.SUCCESS -> Success100
        BadgeVariant.WARNING -> Warning100
        BadgeVariant.DANGER  -> Danger100
        BadgeVariant.INFO    -> Brand100
        BadgeVariant.GOLD    -> Gold100
        BadgeVariant.GRAY    -> Gray100
    }

    val textColor: Color = when (variant) {
        BadgeVariant.SUCCESS -> Success600
        BadgeVariant.WARNING -> Warning600
        BadgeVariant.DANGER  -> Danger600
        BadgeVariant.INFO    -> Brand700
        BadgeVariant.GOLD    -> Gold600
        BadgeVariant.GRAY    -> Gray600
    }

    Surface(
        shape = CircleShape,
        color = backgroundColor,
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}

