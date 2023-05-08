package com.example.simplemedicplan.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.simplemedicplan.application.theme.SimpleMedicPlanTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyEdgeToEdge()
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )

                onDispose {}
            }

            SimpleMedicPlanTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MedicPlanNavHost(
                        modifier = Modifier.fillMaxSize(),
                        startDestination = provideStartDestination()
                    )
                }
            }
        }
    }

    private fun provideStartDestination(): String {
        val isLoggedIn = Firebase.auth.currentUser != null
        val isEmailVerified = isLoggedIn && Firebase.auth.currentUser!!.isEmailVerified
        return when {
            isEmailVerified -> NavDirections.HOME.route
            isLoggedIn -> NavDirections.REGISTRATION_NOTICE.route
            else -> NavDirections.AUTH.route
        }
    }

    private fun applyEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
