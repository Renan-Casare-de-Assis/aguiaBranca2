package com.aguiabranca.inovacao.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.aguiabranca.inovacao.ui.theme.Brand700
import com.aguiabranca.inovacao.ui.theme.Gray400
import com.aguiabranca.inovacao.ui.theme.White

@Composable
fun AguiaBottomNav(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    icons: List<Pair<ImageVector, ImageVector>>? = null
) {
    val defaultIcons = listOf(
        Icons.Filled.Home to Icons.Outlined.Home,
        Icons.Filled.Lightbulb to Icons.Outlined.Lightbulb,
        Icons.Filled.Person to Icons.Outlined.Person
    )
    val resolvedIcons = icons ?: defaultIcons

    NavigationBar(
        containerColor = White,
        tonalElevation = 0.dp
    ) {
        items.forEachIndexed { index, label ->
            val isSelected = selectedIndex == index
            val iconPair = resolvedIcons.getOrNull(index)
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(index) },
                label = {
                    Text(
                        label,
                        color = if (isSelected) Brand700 else Gray400
                    )
                },
                icon = {
                    if (iconPair != null) {
                        Icon(
                            imageVector = if (isSelected) iconPair.first else iconPair.second,
                            contentDescription = label,
                            tint = if (isSelected) Brand700 else Gray400
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Brand700,
                    unselectedIconColor = Gray400,
                    selectedTextColor = Brand700,
                    unselectedTextColor = Gray400
                )
            )
        }
    }
}

