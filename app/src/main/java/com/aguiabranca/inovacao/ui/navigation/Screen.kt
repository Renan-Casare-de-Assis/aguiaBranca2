package com.aguiabranca.inovacao.ui.navigation

sealed class Screen(val route: String) {

    // Auth
    object Login : Screen("login")

    // Operador
    object HomeOperator       : Screen("home_operator")
    object NewIdea            : Screen("new_idea")
    object MyIdeas            : Screen("my_ideas")
    object GuidelinesOperator : Screen("guidelines_operator")
    object ProfileOperator    : Screen("profile_operator")

    // Gestor
    object Curation          : Screen("curation")
    object Projects          : Screen("projects")
    object ProjectForm       : Screen("project_form?ideaId={ideaId}") {
        fun createRoute(ideaId: String? = null) =
            if (ideaId != null) "project_form?ideaId=$ideaId" else "project_form"
    }
    object GuidelinesManager : Screen("guidelines_manager")
    object ProfileManager    : Screen("profile_manager")

    // Liderança
    object Dashboard           : Screen("dashboard")
    object Portfolio           : Screen("portfolio")
    object GuidelinesCrud      : Screen("guidelines_crud")
    object ProfileLeadership   : Screen("profile_leadership")
}

