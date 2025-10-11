package com.example.openhands.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Welcome : Screen("welcome_screen")
    object Login: Screen("login")
}