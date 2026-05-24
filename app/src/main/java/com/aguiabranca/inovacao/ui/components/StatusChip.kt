package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.model.ProjectStatus

@Composable
fun StatusChip(status: String) {
    val (bg, label) = when (status.uppercase()) {
        "NEW", "NOVA"               -> Color(0xFF1565C0) to "Nova"
        "UNDER_REVIEW", "EM_ANALISE"-> Color(0xFFF57F17) to "Em Análise"
        "APPROVED", "APROVADA"      -> Color(0xFF2E7D32) to "Aprovada"
        "REJECTED", "REJEITADA"     -> Color(0xFFC62828) to "Rejeitada"
        "PRIORITIZED","PRIORIZADA"  -> Color(0xFF6A1B9A) to "Priorizada"
        "IN_PROGRESS","EM_ANDAMENTO"-> Color(0xFF00838F) to "Em Andamento"
        "COMPLETED", "CONCLUIDA",
        "CONCLUÍDA"                 -> Color(0xFF558B2F) to "Concluída"
        else                        -> Color(0xFF616161) to status
    }
    Text(
        text = label,
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(bg, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

@Composable
fun IdeaStatusChip(status: IdeaStatus) {
    val (label, variant) = when (status) {
        IdeaStatus.NEW          -> "Nova"        to BadgeVariant.INFO
        IdeaStatus.UNDER_REVIEW -> "Em análise"  to BadgeVariant.WARNING
        IdeaStatus.PRIORITIZED  -> "Priorizada"  to BadgeVariant.GOLD
        IdeaStatus.APPROVED     -> "Aprovada"    to BadgeVariant.SUCCESS
        IdeaStatus.REJECTED     -> "Reprovada"   to BadgeVariant.DANGER
        IdeaStatus.IN_PROJECT   -> "Em projeto"  to BadgeVariant.INFO
    }
    AguiaBadge(text = label, variant = variant)
}

@Composable
fun ProjectStatusChip(status: ProjectStatus) {
    val (label, variant) = when (status) {
        ProjectStatus.ON_TRACK  -> "No prazo"   to BadgeVariant.SUCCESS
        ProjectStatus.AT_RISK   -> "Em risco"   to BadgeVariant.WARNING
        ProjectStatus.DELAYED   -> "Atrasado"   to BadgeVariant.DANGER
        ProjectStatus.COMPLETED -> "Concluído"  to BadgeVariant.SUCCESS
        ProjectStatus.CANCELED  -> "Cancelado"  to BadgeVariant.GRAY
    }
    AguiaBadge(text = label, variant = variant)
}

