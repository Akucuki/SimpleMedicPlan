package com.example.simplemedicplan.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplemedicplan.feature.auth.email.register.EmailAuthScreen
import com.example.simplemedicplan.feature.auth.initial.AuthScreen
import com.example.simplemedicplan.feature.home.HomeScreen

enum class NavDirections(val route: String) {
    AUTH("auth"),
    EMAIL_LOGIN("email_login"),
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
        with (NavDirections.AUTH) {
            composable(route) {
                AuthScreen(
                    onNavigateHome = {
                        navHostController.navigate(
                            NavDirections.HOME.route,
                            NavOptions.Builder().setPopUpTo(route, true).build()
                        )
                    },
                    onNavigateEmailLogin = {
                        navHostController.navigate(NavDirections.EMAIL_LOGIN.route)
                    },
                    onNavigateEmailRegister = {
                        navHostController.navigate(NavDirections.EMAIL_LOGIN.route)
                    }
                )
            }
        }
        composable(NavDirections.EMAIL_LOGIN.route) {
            EmailAuthScreen(
                onNavigateBack = {
                    navHostController.popBackStack()
                },
                onNavigateToRegistrationNotice = {

                }
            )
        }
        composable(NavDirections.HOME.route) {
            HomeScreen()
        }
    }
}