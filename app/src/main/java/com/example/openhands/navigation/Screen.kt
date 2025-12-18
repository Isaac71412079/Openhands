package com.example.openhands.navigation

sealed class Screen(val route: String) {
    // CAMBIO: Se reemplazan Splash y Welcome por una única ruta
    object SplashAndWelcome : Screen("splash_welcome_screen")
    object PrivacyPolicy : Screen("privacy_policy_screen")
    object Login: Screen("login_screen")
    object Register : Screen("register_screen") // Ruta para la pantalla de registro
    object Home: Screen("home_screen")
    object Settings : Screen("settings_screen") // <-- AÑADIDO

    object TextSign : Screen("text_sign_screen")

    object SignCamera : Screen("sign_camera_screen")

    object History: Screen("history_screen")
    object MoreLanguagesWebView : Screen("more_languages_webview")
}