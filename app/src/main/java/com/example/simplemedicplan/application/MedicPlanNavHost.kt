package com.example.simplemedicplan.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplemedicplan.feature.auth.initial.AuthScreen
import com.example.simplemedicplan.feature.home.HomeScreen

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
        composable("auth") {
            AuthScreen(
                onNavigateHome = {
                    navHostController.navigate(
                        "home",
                        NavOptions.Builder().setPopUpTo("auth", true).build()
                    )
                }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}