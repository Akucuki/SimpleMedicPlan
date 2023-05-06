package com.example.simplemedicplan.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplemedicplan.feature.auth.email.register.EmailAuthScreen
import com.example.simplemedicplan.feature.auth.email.registerNotice.EmailRegistrationNoticeScreen
import com.example.simplemedicplan.feature.auth.initial.AuthScreen
import com.example.simplemedicplan.feature.home.HomeScreen

enum class NavDirections(val route: String) {
    AUTH("auth"),
    EMAIL_REGISTER("email_register"),
    EMAIL_LOGIN("email_login"),
    REGISTRATION_NOTICE("registration_notice"),
    HOME("home")
}

@Composable
fun MedicPlanNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = "auth"
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination
    ) {
        with(NavDirections.AUTH) {
            composable(route) {
                AuthScreen(
                    onNavigateHome = {
                        navHostController.navigate(NavDirections.HOME.route) {
                            popUpTo(route) { inclusive = true }
                        }
                    },
                    onNavigateEmailLogin = {
                        navHostController.navigate(NavDirections.EMAIL_REGISTER.route)
                    },
                    onNavigateEmailRegister = {
                        navHostController.navigate(NavDirections.EMAIL_REGISTER.route)
                    }
                )
            }
        }
        composable(NavDirections.EMAIL_REGISTER.route) {
            EmailAuthScreen(
                onNavigateBack = navHostController::popBackStack,
                onNavigateToRegistrationNotice = {
                    navHostController.navigate(NavDirections.REGISTRATION_NOTICE.route) {
                        popUpTo(NavDirections.AUTH.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavDirections.REGISTRATION_NOTICE.route) {
            EmailRegistrationNoticeScreen()
        }
        composable(NavDirections.HOME.route) {
            HomeScreen()
        }
    }
}