package com.example.simplemedicplan.application

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simplemedicplan.feature.auth.email.login.EmailLoginScreen
import com.example.simplemedicplan.feature.auth.email.register.EmailRegisterScreen
import com.example.simplemedicplan.feature.auth.email.registerNotice.EmailRegistrationNoticeScreen
import com.example.simplemedicplan.feature.auth.initial.AuthScreen
import com.example.simplemedicplan.feature.home.pills.PillsScreen
import com.example.simplemedicplan.feature.home.pills.edit.PillEditScreen
import com.example.simplemedicplan.feature.home.profile.ProfileScreen

enum class NavDirection(val route: String) {
    AUTH("auth"),
    EMAIL_REGISTER("email_register"),
    EMAIL_LOGIN("email_login"),
    REGISTRATION_NOTICE("registration_notice"),
    HOME_SUBROUTE("home"),
    PILLS("${HOME_SUBROUTE.route}/pills"),
    MEDIC_CARD("${HOME_SUBROUTE.route}/medic_card"),
    PROFILE("${HOME_SUBROUTE.route}/profile"),
    PILLS_EDIT("${PILLS.route}/edit"),
}

@Composable
fun MedicPlanNavHost(
    modifier: Modifier = Modifier,
    activity: ComponentActivity,
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = NavDirection.AUTH.route
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination
    ) {
        with(NavDirection.AUTH) {
            composable(route) {
                AuthScreen(
                    onNavigateHome = {
                        navHostController.navigate(NavDirection.PILLS.route) {
                            popUpTo(route) { inclusive = true }
                        }
                    },
                    onNavigateEmailLogin = {
                        navHostController.navigate(NavDirection.EMAIL_LOGIN.route)
                    },
                    onNavigateEmailRegister = {
                        navHostController.navigate(NavDirection.EMAIL_REGISTER.route)
                    }
                )
            }
        }
        composable(NavDirection.EMAIL_REGISTER.route) {
            EmailRegisterScreen(
                onNavigateBack = navHostController::popBackStack,
                onNavigateToRegistrationNotice = {
                    navHostController.navigate(NavDirection.REGISTRATION_NOTICE.route) {
                        popUpTo(NavDirection.AUTH.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavDirection.REGISTRATION_NOTICE.route) {
            EmailRegistrationNoticeScreen(
                onNavigateToHome = {
                    navHostController.navigate(NavDirection.PILLS.route) {
                        popUpTo(NavDirection.AUTH.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavDirection.EMAIL_LOGIN.route) {
            EmailLoginScreen(
                onNavigateBack = navHostController::popBackStack,
                onNavigateToHome = {
                    navHostController.navigate(NavDirection.PILLS.route) {
                        popUpTo(NavDirection.AUTH.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavDirection.PILLS.route) {
            val nestedNavController = rememberNavController()
            if (currentRoute == NavDirection.PILLS.route) {
                BackHandler(
                    onBack = {
                        if (!nestedNavController.navigateUp()) {
                            activity.finish()
                        }
                    }
                )
            }
            PillsScreen(
                onNavigateToEditPill = { pillId ->
                    val route = if (pillId != null) {
                        "${NavDirection.PILLS_EDIT.route}?pillId=$pillId"
                    } else {
                        NavDirection.PILLS_EDIT.route
                    }
                    navHostController.navigate(route)
                }
            )
        }
        composable(NavDirection.MEDIC_CARD.route) {
            val nestedNavController = rememberNavController()
            if (currentRoute == NavDirection.MEDIC_CARD.route) {
                BackHandler(
                    onBack = {
                        if (!nestedNavController.navigateUp()) {
                            activity.finish()
                        }
                    }
                )
            }
        }
        composable(NavDirection.PROFILE.route) {
            val nestedNavController = rememberNavController()
            if (currentRoute == NavDirection.PROFILE.route) {
                BackHandler(
                    onBack = {
                        if (!nestedNavController.navigateUp()) {
                            activity.finish()
                        }
                    }
                )
            }
            ProfileScreen(
                onNavigateToAuth = {
                    navHostController.navigate(NavDirection.AUTH.route) {
                        popUpTo(navHostController.graph.id) { inclusive = true }
                    }
                }
            )
        }
        composable(
            "${NavDirection.PILLS_EDIT.route}?pillId={pillId}",
            arguments = listOf(
                navArgument("pillId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            val pillId = it.arguments?.getString("pillId")
            PillEditScreen(
                onNavigateBack = navHostController::popBackStack,
                pillUuid = pillId
            )
        }
    }
}