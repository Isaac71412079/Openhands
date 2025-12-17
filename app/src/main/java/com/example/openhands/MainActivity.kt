package com.example.openhands

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.openhands.navigation.AppNavigation
import com.example.openhands.ui.theme.OpenhandsTheme

// 🔥 Firebase Imports
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

class MainActivity : ComponentActivity() {

    // NUEVO: Lanzador para pedir permiso de notificaciones
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            Log.d("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Pedir permiso de notificaciones al iniciar (Android 13+)
        askNotificationPermission()

        // 2. Verificar si abrimos la app desde una notificación de "Seña del Día"
        val wordOfTheDay = intent.getStringExtra("WORD_OF_THE_DAY")
        if (wordOfTheDay != null) {
            // Aquí capturas la palabra.
            // Podrías navegar a la pantalla de texto, pero por ahora mostramos un Toast.
            Toast.makeText(this, "¡La seña del día es: $wordOfTheDay!", Toast.LENGTH_LONG).show()
        }

        // Configuración de prueba original (opcional, la dejo si la necesitas)
        // testFirebaseConfig()

        setContent {
            OpenhandsTheme {
                AppNavigation()
            }
        }
    }

    // NUEVO: Función para manejar la lógica de permisos
    private fun askNotificationPermission() {
        // Solo es necesario en Android 13 (Tiramisu - API 33) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Ya tenemos permiso, todo bien.
            } else {
                // Pedir permiso directamente
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun testFirebaseConfig() {
        try {
            val auth = Firebase.auth
            val db = Firebase.firestore
            Log.d("FirebaseTest", "Firebase Auth OK: ${auth != null}")
            Log.d("FirebaseTest", "Firebase Firestore OK: ${db != null}")
            // Comentado para no molestar al usuario final
            // Toast.makeText(this, "Firebase Config OK", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Error: ${e.message}")
        }
    }
}