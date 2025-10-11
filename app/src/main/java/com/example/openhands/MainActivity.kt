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
import com.example.openhands.features.home.presentation.HomeScreen
import com.example.openhands.features.login.presentation.LoginScreen
// CAMBIO: Importamos la nueva pantalla de traducción
import com.example.openhands.features.textsign.presentation.TextSignScreen
import com.example.openhands.features.welcome.presentation.SplashAndWelcomeScreen
import com.example.openhands.navigation.Screen
import com.example.openhands.ui.theme.OpenhandsTheme

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
                                // CAMBIO: Navega a la nueva ruta Screen.TextSign.route
                                rootNavController.navigate(Screen.TextSign.route)
                            },
                            onImageActionClick = {
                                rootNavController.navigate(Screen.ImageAction.route)
                            }
                        )
                    }

                    // CAMBIO: Se reemplazó la ruta de TextAction por la nueva de TextSign
                    composable(Screen.TextSign.route) {
                        TextSignScreen(
                            onNavigateBack = {
                                // Esta lambda permite que el botón de retroceso en la TopAppBar funcione
                                rootNavController.navigateUp()
                            }
                        )
                    }

                    // La pantalla para ImageAction sigue siendo el placeholder
                    composable(Screen.ImageAction.route) {
                        HelloScreen("Image Action")
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