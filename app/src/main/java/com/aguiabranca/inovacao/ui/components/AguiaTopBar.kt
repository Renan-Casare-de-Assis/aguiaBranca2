package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.aguiabranca.inovacao.ui.theme.*

@Composable
fun AguiaTopBar(
    title: String,
    subtitle: String? = null,
    notificationCount: Int = 0,
    userInitials: String = "",
    onNotificationClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Brand900, Brand700)
                )
            )
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = White
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BadgedBox(
                        badge = {
                            if (notificationCount > 0) {
                                Badge(
                                    containerColor = Danger500
                                ) {
                                    Text(
                                        text = "$notificationCount",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = White
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNotificationClick) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificações",
                                tint = White
                            )
                        }
                    }
                    AguiaAvatar(
                        initials = userInitials,
                        size = 38.dp,
                        modifier = Modifier.clickable { onAvatarClick() }
                    )
                }
            }
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

