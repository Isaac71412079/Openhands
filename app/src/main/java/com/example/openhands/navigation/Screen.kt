package com.example.openhands.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
}