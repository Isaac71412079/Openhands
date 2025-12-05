package com.example.openhands

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.openhands.features.auth.presentation.RegisterScreen
import com.example.openhands.features.home.presentation.HomeScreen
import com.example.openhands.features.login.presentation.LoginScreen
import com.example.openhands.features.signcamera.presentation.CapturedImageScreen
import com.example.openhands.features.signcamera.presentation.SignCameraScreen
import com.example.openhands.features.signcamera.presentation.SignCameraViewModel
import com.example.openhands.features.textsign.presentation.TextSignScreen
import com.example.openhands.features.timeprovider.presentation.TimeScreen
import com.example.openhands.features.welcome.presentation.SplashAndWelcomeScreen
import com.example.openhands.navigation.Screen
import com.example.openhands.ui.theme.OpenhandsTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenhandsTheme {
                val context = LocalContext.current

                // 🔹 Prueba de Firebase
                testFirebaseConfig()

                // 🔹 Inicia la navegación desde TimeScreen
                AppNavigation(startDestination = Screen.Time.route)
            }
        }
    }

    private fun testFirebaseConfig() {
        try {
            val auth = Firebase.auth
            val db = Firebase.firestore
            Log.d("FirebaseTest", "Firebase Auth OK: ${auth != null}")
            Log.d("FirebaseTest", "Firebase Firestore OK: ${db != null}")

            Toast.makeText(this, "Firebase Config OK", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Error: ${e.message}")
            Toast.makeText(this, "Firebase ERROR: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun AppNavigation(startDestination: String = Screen.SplashAndWelcome.route) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
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
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SplashAndWelcome.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onTextActionClick = { navController.navigate(Screen.TextSign.route) },
                onImageActionClick = { navController.navigate(Screen.SignCamera.route) }
            )
        }

        composable(Screen.TextSign.route) {
            TextSignScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable(Screen.SignCamera.route) {
            SignCameraScreen(
                rootNavController = navController,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(Screen.CapturedImage.route) {
            val viewModel = koinViewModel<SignCameraViewModel>()
            val imageUri = viewModel.capturedImageUri
            CapturedImageScreen(
                imageUri = imageUri,
                onRetakePhoto = { navController.popBackStack() },
                onConfirm = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Time.route) {
            TimeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}