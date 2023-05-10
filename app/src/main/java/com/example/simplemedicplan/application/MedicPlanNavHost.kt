package com.example.simplemedicplan.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplemedicplan.feature.auth.email.login.EmailLoginScreen
import com.example.simplemedicplan.feature.auth.email.register.EmailRegisterScreen
import com.example.simplemedicplan.feature.auth.email.registerNotice.EmailRegistrationNoticeScreen
import com.example.simplemedicplan.feature.auth.initial.AuthScreen
import com.example.simplemedicplan.feature.home.pills.PillsScreen

enum class NavDirection(val route: String) {
    AUTH("auth"),
    EMAIL_REGISTER("email_register"),
    EMAIL_LOGIN("email_login"),
    REGISTRATION_NOTICE("registration_notice"),
    HOME_SUBROUTE("home"),
    PILLS("${HOME_SUBROUTE.route}/pills"),
    MEDIC_CARD("${HOME_SUBROUTE.route}/medic_card"),
    PROFILE("${HOME_SUBROUTE.route}/profile")
}

@Composable
fun MedicPlanNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = NavDirection.AUTH.route
) {
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
            PillsScreen(onNavigateToAddPill = {})
        }
        composable(NavDirection.MEDIC_CARD.route) {
            PillsScreen(onNavigateToAddPill = {})
        }
        composable(NavDirection.PROFILE.route) {
            PillsScreen(onNavigateToAddPill = {})
        }
    }
}