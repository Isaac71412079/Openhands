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
// CAMBIO: Importamos la nueva pantalla combinada
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
                    // CAMBIO: La ruta de inicio ahora es la nueva pantalla combinada.
                    startDestination = Screen.SplashAndWelcome.route
                ) {
                    // CAMBIO: Se eliminaron los composables para 'Splash' y 'Welcome'.
                    // Ahora tenemos un único composable para la experiencia inicial.
                    composable(Screen.SplashAndWelcome.route) {
                        SplashAndWelcomeScreen(
                            onLoginClicked = {
                                rootNavController.navigate(Screen.Login.route) {
                                    // Limpiamos el backstack para que el usuario no pueda
                                    // volver a la pantalla de bienvenida con el botón de retroceso.
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
                                    // CAMBIO: Al iniciar sesión, eliminamos la pantalla de login del backstack.
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // Las otras rutas no han cambiado.
                    composable(Screen.Home.route) {
                        HomeScreen(
                            onTextActionClick = {
                                rootNavController.navigate(Screen.TextAction.route)
                            },
                            onImageActionClick = {
                                rootNavController.navigate(Screen.ImageAction.route)
                            }
                        )
                    }

                    composable(Screen.TextAction.route) {
                        HelloScreen("Text Action")
                    }

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