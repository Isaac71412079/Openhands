package com.example.openhands.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.openhands.features.welcome.presentation.SplashAndWelcomeScreen
import com.example.openhands.features.auth.presentation.RegisterScreen
import com.example.openhands.features.login.presentation.LoginScreen
import com.example.openhands.features.signcamera.presentation.CapturedImageScreen
import com.example.openhands.features.signcamera.presentation.SignCameraViewModel
import org.koin.androidx.compose.koinViewModel
import android.net.Uri
import com.example.openhands.features.timeprovider.presentation.TimeScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Time.route
    ) {

        // Pantalla de bienvenida
        composable(Screen.SplashAndWelcome.route) {
            SplashAndWelcomeScreen(
                onLoginClicked = { navController.navigate(Screen.Login.route) },
                onRegisterClicked = { navController.navigate(Screen.Register.route) }
            )
        }

        // Pantalla de login
        composable(Screen.Login.route) {
            LoginScreen()
        }

        //Pantalla del time provider
        composable(Screen.Time.route) {
            TimeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de registro
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SplashAndWelcome.route) { inclusive = false }
                    }
                }
            )
        }

        // Pantalla de imagen capturada
        composable(Screen.CapturedImage.route) {
            val viewModel = koinViewModel<SignCameraViewModel>()
            val imageUri = viewModel.capturedImageUri

            CapturedImageScreen(
                imageUri = imageUri,
                onRetakePhoto = { navController.popBackStack() },
                onConfirm = { navController.navigate(Screen.Login.route) }
            )
        }


    }
}