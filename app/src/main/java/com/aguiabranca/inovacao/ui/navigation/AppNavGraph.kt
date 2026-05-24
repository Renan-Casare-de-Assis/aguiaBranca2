package com.aguiabranca.inovacao.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.ui.auth.LoginScreen
import com.aguiabranca.inovacao.ui.operator.GuidelinesOperatorScreen
import com.aguiabranca.inovacao.ui.operator.HomeOperatorScreen
import com.aguiabranca.inovacao.ui.operator.MyIdeasScreen
import com.aguiabranca.inovacao.ui.operator.NewIdeaScreen
import com.aguiabranca.inovacao.ui.operator.ProfileOperatorScreen
import com.aguiabranca.inovacao.ui.manager.CurationScreen
import com.aguiabranca.inovacao.ui.manager.GuidelinesManagerScreen
import com.aguiabranca.inovacao.ui.manager.ProfileManagerScreen
import com.aguiabranca.inovacao.ui.manager.ProjectFormScreen
import com.aguiabranca.inovacao.ui.manager.ProjectsScreen
import com.aguiabranca.inovacao.ui.leadership.DashboardScreen
import com.aguiabranca.inovacao.ui.leadership.GuidelinesCrudScreen
import com.aguiabranca.inovacao.ui.leadership.PortfolioScreen
import com.aguiabranca.inovacao.ui.leadership.ProfileLeadershipScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    sessionManager: SessionManager
) {
    val currentUser by sessionManager.currentUser.collectAsState()

    val startDestination = when (currentUser?.role) {
        UserRole.OPERATOR   -> Screen.HomeOperator.route
        UserRole.MANAGER    -> Screen.Curation.route
        UserRole.LEADERSHIP -> Screen.Dashboard.route
        null                -> Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ── Auth ──────────────────────────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { user ->
                    val dest = when (user.role) {
                        UserRole.OPERATOR   -> Screen.HomeOperator.route
                        UserRole.MANAGER    -> Screen.Curation.route
                        UserRole.LEADERSHIP -> Screen.Dashboard.route
                    }
                    navController.navigate(dest) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Operador ──────────────────────────────────────────────────────
        composable(Screen.HomeOperator.route) {
            HomeOperatorScreen(
                onNavigateToNewIdea   = { navController.navigate(Screen.NewIdea.route) },
                onNavigateToMyIdeas   = { navController.navigate(Screen.MyIdeas.route) },
                onNavigateToGuidelines = { navController.navigate(Screen.GuidelinesOperator.route) },
                onNavigateToProfile   = { navController.navigate(Screen.ProfileOperator.route) }
            )
        }
        composable(Screen.NewIdea.route) {
            NewIdeaScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.HomeOperator.route) {
                        popUpTo(Screen.HomeOperator.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateToIdeas = {
                    navController.navigate(Screen.MyIdeas.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.ProfileOperator.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.MyIdeas.route) {
            MyIdeasScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHome = { navController.popBackStack() },
                onNavigateToNewIdea = { navController.navigate(Screen.NewIdea.route) },
                onNavigateToProfile = { navController.navigate(Screen.ProfileOperator.route) }
            )
        }
        composable(Screen.GuidelinesOperator.route) {
            GuidelinesOperatorScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.ProfileOperator.route) {
            ProfileOperatorScreen(
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Gestor ────────────────────────────────────────────────────────
        composable(Screen.Curation.route) {
            CurationScreen(
                onNavigateToProjectForm = { ideaId ->
                    navController.navigate(Screen.ProjectForm.createRoute(ideaId))
                },
                onNavigateToProjects    = { navController.navigate(Screen.Projects.route) },
                onNavigateToGuidelines  = { navController.navigate(Screen.GuidelinesManager.route) },
                onNavigateToProfile     = { navController.navigate(Screen.ProfileManager.route) }
            )
        }
        composable(Screen.Projects.route) {
            ProjectsScreen(
                onNavigateToProjectForm = { navController.navigate(Screen.ProjectForm.createRoute()) },
                onBack                  = { navController.popBackStack() },
                onNavigateToCuration    = {
                    navController.navigate(Screen.Curation.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile     = {
                    navController.navigate(Screen.ProfileManager.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Screen.ProjectForm.route,
            arguments = listOf(navArgument("ideaId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val ideaId = backStackEntry.arguments?.getString("ideaId")
            ProjectFormScreen(
                ideaId = ideaId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.GuidelinesManager.route) {
            GuidelinesManagerScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.ProfileManager.route) {
            ProfileManagerScreen(
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Liderança ─────────────────────────────────────────────────────
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToPortfolio   = { navController.navigate(Screen.Portfolio.route) },
                onNavigateToGuidelines  = { navController.navigate(Screen.GuidelinesCrud.route) },
                onNavigateToProfile     = { navController.navigate(Screen.ProfileLeadership.route) }
            )
        }
        composable(Screen.Portfolio.route) {
            PortfolioScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToGuidelines = {
                    navController.navigate(Screen.GuidelinesCrud.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.ProfileLeadership.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.GuidelinesCrud.route) {
            GuidelinesCrudScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.ProfileLeadership.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.ProfileLeadership.route) {
            ProfileLeadershipScreen(
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

