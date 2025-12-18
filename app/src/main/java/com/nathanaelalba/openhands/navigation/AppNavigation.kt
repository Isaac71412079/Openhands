package com.nathanaelalba.openhands.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nathanaelalba.openhands.features.auth.presentation.RegisterScreen
import com.nathanaelalba.openhands.features.home.presentation.HistoryScreen
import com.nathanaelalba.openhands.features.home.presentation.HomeScreen
import com.nathanaelalba.openhands.features.home.presentation.HomeViewModel
import com.nathanaelalba.openhands.features.login.presentation.LoginScreen
import com.nathanaelalba.openhands.features.privacy_policy.PrivacyPolicyScreen
import com.nathanaelalba.openhands.features.settings.presentation.SettingsScreen
import com.nathanaelalba.openhands.features.signcamera.presentation.SignCameraScreen
import com.nathanaelalba.openhands.features.textsign.presentation.TextSignScreen
import com.nathanaelalba.openhands.features.textsign.presentation.WebViewScreen
import com.nathanaelalba.openhands.features.welcome.presentation.SplashAndWelcomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashAndWelcome.route
    ) {
        composable(Screen.SplashAndWelcome.route) {
            SplashAndWelcomeScreen(
                onLoginClicked = { navController.navigate(Screen.Login.route) },
                onRegisterClicked = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.navigateUp() },
                onRegisterClicked = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(Screen.Home.route) {
            // --- LÍNEAS CORREGIDAS ---
            val homeViewModel: HomeViewModel = koinViewModel()
            val userEmail by homeViewModel.userEmail.collectAsState()

            HomeScreen(
                userEmail = userEmail, // <-- Parámetro restaurado
                onTextActionClick = { navController.navigate(Screen.TextSign.route) },
                onCameraActionClick = { navController.navigate(Screen.SignCamera.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onLogout = {
                    homeViewModel.logout() // <-- Llamada restaurada
                    navController.navigate(Screen.SplashAndWelcome.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() },
                onPrivacyPolicyClick = { navController.navigate(Screen.PrivacyPolicy.route) } 
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                showAppBar = true, 
                onNavigateBack = { navController.navigateUp() }
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