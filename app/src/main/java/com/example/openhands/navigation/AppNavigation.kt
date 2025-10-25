package com.example.openhands.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.openhands.features.welcome.presentation.SplashAndWelcomeScreen
import com.example.openhands.features.auth.presentation.RegisterScreen
import com.example.openhands.features.login.presentation.LoginScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashAndWelcome.route // 👈 empieza desde tu pantalla de bienvenida
    ) {

        // 🟦 Pantalla de bienvenida / splash
        composable(Screen.SplashAndWelcome.route) {
            SplashAndWelcomeScreen(
                onLoginClicked = { navController.navigate(Screen.Login.route) },
                onRegisterClicked = { navController.navigate(Screen.Register.route) }
            )
        }

        // 🟨 Pantalla de login
        composable(Screen.Login.route) {
            LoginScreen()
        }

        // 🟩 Pantalla de registro
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navegamos al login luego del registro exitoso
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SplashAndWelcome.route) { inclusive = false }
                    }
                }
            )
        }

        // (Opcional) si luego quieres ir al Home
        // composable(Screen.Home.route) {
        //     HomeScreen()
        // }
    }
}
