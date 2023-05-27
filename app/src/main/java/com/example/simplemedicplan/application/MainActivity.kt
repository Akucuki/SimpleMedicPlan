package com.example.simplemedicplan.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.simplemedicplan.application.theme.DarkBlueColor
import com.example.simplemedicplan.application.theme.SimpleMedicPlanTheme
import com.example.simplemedicplan.feature.home.HomeNavigationBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEdgeToEdge()
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            val layoutDirection = LocalLayoutDirection.current
            val navController = rememberNavController()
            var isNavigationBarVisible by remember { mutableStateOf(false) }

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    isNavigationBarVisible =
                        destination.route?.startsWith(NavDirection.HOME_SUBROUTE.route) ?: false
                }

                onDispose {}
            }

            SimpleMedicPlanTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (isNavigationBarVisible) {
                            HomeNavigationBar(
                                modifier = Modifier,
                                onNavigate = {
                                    navController.navigate(it.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                    }
                                }
                            )
                        }
                    },
                    containerColor = DarkBlueColor
                ) {
                    // Caution!
                    val paddingValuesExceptTop = PaddingValues(
                        start = it.calculateStartPadding(layoutDirection),
                        end = it.calculateEndPadding(layoutDirection),
                        bottom = it.calculateBottomPadding()
                    )
                    MedicPlanNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValuesExceptTop),
                        startDestination = provideStartDestination(),
                        navHostController = navController,
                        activity = this
                    )
                }
            }
        }
    }

    private fun provideStartDestination(): String {
        val isLoggedIn = Firebase.auth.currentUser != null
        val isEmailVerified = isLoggedIn && Firebase.auth.currentUser!!.isEmailVerified
        return when {
            isEmailVerified -> NavDirection.PILLS.route
            isLoggedIn -> NavDirection.REGISTRATION_NOTICE.route
            else -> NavDirection.AUTH.route
        }
    }

    private fun applyEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
