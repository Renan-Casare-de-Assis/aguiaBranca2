package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.ui.theme.*

data class ProfileOverviewRow(
    val label: String,
    val value: String
)

data class ProfileSettingRow(
    val icon: String,
    val label: String
)

data class ProfileScreenConfig(
    val roleBadge: String,
    val area: String,
    val coverage: String,
    val role: String,
    val invested: String,
    val returnValue: String,
    val overviewBadge: String,
    val overviewBadgeBackground: Color = ProfilePositiveBg,
    val overviewBadgeTextColor: Color = ProfilePositiveText,
    val overviewRows: List<ProfileOverviewRow>,
    val settings: List<ProfileSettingRow>,
    val footer: String = "Versão 1.0.0 • Challenge FIAP 2026"
)

@Composable
fun AguiaProfileScreen(
    user: User?,
    config: ProfileScreenConfig,
    onLogout: () -> Unit,
) {
    val displayName = user?.name?.takeIf { it.isNotBlank() } ?: "Meu Perfil"
    val displayEmail = user?.email?.takeIf { it.isNotBlank() } ?: ""
    val initials = remember(displayName) { buildInitials(displayName) }

    Scaffold(
        containerColor = ProfilePageBackground,
        topBar = {
            ProfileTopBar(
                title = "Meu Perfil",
                subtitle = "Gerencie suas informações",
                initials = initials
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileHeroCard(
                name = displayName,
                email = displayEmail,
                badgeText = config.roleBadge
            )

            ProfileSectionHeader(title = "Informações executivas")

            AguiaCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProfileInfoRow(label = "Área", value = config.area)
                    ProfileInfoRow(label = "Abrangência", value = config.coverage)
                    ProfileInfoRow(label = "Perfil", value = config.role)
                }
            }

            ProfileSectionHeader(title = "Impacto estratégico")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileMetricCard(
                    title = "INVESTIDO",
                    value = config.invested,
                    backgroundColor = ProfileDarkMetric,
                    modifier = Modifier.weight(1f)
                )
                ProfileMetricCard(
                    title = "RETORNO",
                    value = config.returnValue,
                    backgroundColor = ProfileGreenMetric,
                    modifier = Modifier.weight(1f)
                )
            }

            AguiaCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Visão geral",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProfilePrimaryText
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(config.overviewBadgeBackground)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = config.overviewBadge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = config.overviewBadgeTextColor
                            )
                        }
                    }

                    config.overviewRows.forEachIndexed { index, row ->
                        if (index > 0) {
                            HorizontalDivider(color = ProfileSurfaceBorder)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = row.label,
                                fontSize = 13.sp,
                                color = ProfileSecondaryText
                            )
                            Text(
                                text = row.value,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ProfilePrimaryText
                            )
                        }
                    }
                }
            }

            ProfileSectionHeader(title = "Configurações")

            AguiaCard {
                Column {
                    config.settings.forEachIndexed { index, item ->
                        if (index > 0) {
                            HorizontalDivider(color = ProfileSurfaceBorder)
                        }
                        ProfileSettingItemRow(item = item)
                    }
                }
            }

            AguiaCard(onClick = onLogout) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⎋", fontSize = 16.sp, color = ProfileActionText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sair da conta",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProfileActionText
                    )
                }
            }

            Text(
                text = config.footer,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 10.sp,
                color = ProfileMutedText,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileTopBar(
    title: String,
    subtitle: String,
    initials: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Brand900, Brand700)
                )
            )
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = White.copy(alpha = 0.78f)
                )
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2B426F)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun ProfileHeroCard(
    name: String,
    email: String,
    badgeText: String
) {
    AguiaCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(Dourado),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildInitials(name).firstOrNull()?.toString() ?: "?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarinho
                )
            }

            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ProfilePrimaryText
            )
            if (email.isNotBlank()) {
                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = ProfileSecondaryText
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(ProfileChipBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "✦ $badgeText",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProfileChipText
                )
            }
        }
    }
}

@Composable
private fun ProfileSectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = ProfileSecondaryText
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = ProfileSurfaceBorder
        )
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Dourado.copy(alpha = 0.18f))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = ProfileSecondaryText
            )
        }
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = ProfilePrimaryText
        )
    }
}

@Composable
private fun ProfileMetricCard(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(84.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = White.copy(alpha = 0.78f)
                )
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun ProfileSettingItemRow(item: ProfileSettingRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(ProfilePageBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.icon, fontSize = 13.sp)
        }
        Text(
            text = item.label,
            modifier = Modifier.weight(1f),
            fontSize = 13.sp,
            color = ProfilePrimaryText
        )
        Text(
            text = "›",
            fontSize = 18.sp,
            color = ProfileMutedText
        )
    }
}

private fun buildInitials(name: String): String {
    val parts = name.trim().split(Regex("\\s+"))
        .filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts.first().take(1).uppercase()
        else -> parts.take(2).joinToString("") { it.first().uppercase() }
    }
}

fun leadershipProfileConfig(): ProfileScreenConfig = ProfileScreenConfig(
    roleBadge = "Diretoria de Inovação",
    area = "Diretoria de Inovação",
    coverage = "5 departamentos • 320 colaboradores",
    role = "Liderança",
    invested = "R$ 2,4M",
    returnValue = "R$ 4,1M",
    overviewBadge = "POSITIVO",
    overviewRows = listOf(
        ProfileOverviewRow("Orientações ativas", "4"),
        ProfileOverviewRow("Projetos no portfólio", "12"),
        ProfileOverviewRow("Em risco", "2")
    ),
    settings = listOf(
        ProfileSettingRow("🔔", "Notificações"),
        ProfileSettingRow("🔒", "Alterar senha"),
        ProfileSettingRow("❔", "Central de ajuda")
    )
)

fun managerProfileConfig(): ProfileScreenConfig = ProfileScreenConfig(
    roleBadge = "Gestão de Inovação",
    area = "Gestão de Inovação",
    coverage = "5 departamentos • 320 colaboradores",
    role = "Gestão",
    invested = "R$ 1,8M",
    returnValue = "R$ 3,2M",
    overviewBadge = "EM DIA",
    overviewBadgeBackground = ProfilePositiveBg,
    overviewBadgeTextColor = ProfilePositiveText,
    overviewRows = listOf(
        ProfileOverviewRow("Curadorias pendentes", "4"),
        ProfileOverviewRow("Projetos no portfólio", "12"),
        ProfileOverviewRow("Acompanhamentos", "8")
    ),
    settings = listOf(
        ProfileSettingRow("🔔", "Notificações"),
        ProfileSettingRow("🔒", "Alterar senha"),
        ProfileSettingRow("❔", "Central de ajuda")
    )
)

fun operatorProfileConfig(): ProfileScreenConfig = ProfileScreenConfig(
    roleBadge = "Operação de Ideias",
    area = "Operação de Ideias",
    coverage = "3 fluxos • 42 colaboradores",
    role = "Operação",
    invested = "R$ 580k",
    returnValue = "R$ 1,1M",
    overviewBadge = "ATIVO",
    overviewBadgeBackground = ProfilePositiveBg,
    overviewBadgeTextColor = ProfilePositiveText,
    overviewRows = listOf(
        ProfileOverviewRow("Ideias enviadas", "18"),
        ProfileOverviewRow("Em andamento", "6"),
        ProfileOverviewRow("Aprovadas", "11")
    ),
    settings = listOf(
        ProfileSettingRow("🔔", "Notificações"),
        ProfileSettingRow("🔒", "Alterar senha"),
        ProfileSettingRow("❔", "Central de ajuda")
    )
)
