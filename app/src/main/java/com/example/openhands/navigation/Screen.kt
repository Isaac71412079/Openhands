package com.example.openhands.navigation

sealed class Screen(val route: String) {
    object SplashAndWelcome : Screen("splash_welcome_screen")
    object Register : Screen("register")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object TextSign : Screen("text_sign_screen")
    object SignCamera : Screen("sign_camera_screen")
    object CapturedImage : Screen("captured_image")
    object Time : Screen("TimeScreen") // OK
}
