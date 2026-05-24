package com.aguiabranca.inovacao.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.aguiabranca.inovacao.ui.theme.AzulMarinho
import com.aguiabranca.inovacao.ui.theme.Dourado

@Composable
fun AguiaBottomNav(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(containerColor = AzulMarinho) {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                label = { Text(label, color = if (selectedIndex == index) Dourado else Color.Gray) },
                icon = {},
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Dourado.copy(alpha = 0.2f)
                )
            )
        }
    }
}

