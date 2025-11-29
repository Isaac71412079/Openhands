package com.example.openhands

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.openhands.features.home.presentation.HistoryScreen
import com.example.openhands.features.home.presentation.HomeScreen
import com.example.openhands.features.login.presentation.LoginScreen
import com.example.openhands.features.signcamera.presentation.SignCameraScreen
import com.example.openhands.features.textsign.presentation.TextSignScreen
import com.example.openhands.features.welcome.presentation.SplashAndWelcomeScreen
import com.example.openhands.navigation.Screen
import com.example.openhands.ui.theme.OpenhandsTheme
import com.example.openhands.features.textsign.presentation.WebViewScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenhandsTheme {
                val rootNavController = rememberNavController()
                val context = LocalContext.current

                NavHost(
                    navController = rootNavController,
                    startDestination = Screen.SplashAndWelcome.route
                ) {
                    composable(Screen.SplashAndWelcome.route) {
                        SplashAndWelcomeScreen(
                            onLoginClicked = {
                                rootNavController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.SplashAndWelcome.route) { inclusive = true }
                                }
                            },
                            onRegisterClicked = {
                                Toast.makeText(context, "Función no disponible aún", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginSuccess = {
                                rootNavController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(
                            onTextActionClick = {
                                rootNavController.navigate(Screen.TextSign.route)
                            },
                            onImageActionClick = {
                                rootNavController.navigate(Screen.SignCamera.route)
                            },
                            onHistoryClick = {
                                rootNavController.navigate(Screen.History.route)
                            }
                        )
                    }

                    composable(Screen.History.route) {
                        HistoryScreen(
                            onNavigateBack = {
                                rootNavController.navigateUp()
                            }
                        )
                    }

                    composable(Screen.TextSign.route) {
                        TextSignScreen(
                            onNavigateBack = {
                                rootNavController.navigateUp()
                            },
                            onNavigateToMoreLanguages = {
                                rootNavController.navigate(Screen.MoreLanguagesWebView.route)
                            }
                        )
                    }

                    composable(Screen.MoreLanguagesWebView.route) {
                        WebViewScreen(
                            url = "https://sign.mt/",
                            onNavigateBack = {
                                rootNavController.navigateUp()
                            }
                        )
                    }

                    composable(Screen.SignCamera.route) {
                        SignCameraScreen(
                            onNavigateBack = {
                                rootNavController.navigateUp()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelloScreen(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello $name!"
        )
    }
}