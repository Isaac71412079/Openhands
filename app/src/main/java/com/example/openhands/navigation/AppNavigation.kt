package com.example.openhands.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.openhands.features.auth.presentation.RegisterScreen
import com.example.openhands.features.home.presentation.HistoryScreen
import com.example.openhands.features.home.presentation.HomeScreen
import com.example.openhands.features.login.presentation.LoginScreen
import com.example.openhands.features.signcamera.presentation.SignCameraScreen
import com.example.openhands.features.textsign.presentation.TextSignScreen
import com.example.openhands.features.textsign.presentation.WebViewScreen
import com.example.openhands.features.welcome.presentation.SplashAndWelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashAndWelcome.route
    ) {
        composable(Screen.SplashAndWelcome.route) {
            SplashAndWelcomeScreen(
                onLoginClicked = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SplashAndWelcome.route) { inclusive = true }
                    }
                },
                onRegisterClicked = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onTextActionClick = { navController.navigate(Screen.TextSign.route) },
                onImageActionClick = { navController.navigate(Screen.SignCamera.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable(Screen.TextSign.route) {
            TextSignScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToMoreLanguages = { navController.navigate(Screen.MoreLanguagesWebView.route) }
            )
        }

        composable(Screen.MoreLanguagesWebView.route) {
            WebViewScreen(
                url = "https://sign.mt/",
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(Screen.SignCamera.route) {
            SignCameraScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}