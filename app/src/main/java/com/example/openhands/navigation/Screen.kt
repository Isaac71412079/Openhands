package com.example.openhands.navigation

sealed class Screen(val route: String) {
    // CAMBIO: Se reemplazan Splash y Welcome por una única ruta
    object SplashAndWelcome : Screen("splash_welcome_screen")

    object Login: Screen("login_screen")
    object Home: Screen("home_screen")

    object TextSign : Screen("text_sign_screen")

    object SignCamera : Screen("sign_camera_screen")}