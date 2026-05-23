package com.aguiabranca.inovacao.ui.components

import androidx.compose.runtime.Composable
import com.aguiabranca.inovacao.domain.model.IdeaStatus
import com.aguiabranca.inovacao.domain.model.ProjectStatus

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

