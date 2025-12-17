package com.nathanaelalba.openhands

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import com.nathanaelalba.openhands.features.appupdate.domain.AppUpdateConfig
import com.nathanaelalba.openhands.features.appupdate.presentation.AppUpdateManager
import com.nathanaelalba.openhands.navigation.AppNavigation
import com.nathanaelalba.openhands.ui.theme.OpenhandsTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---------------------------- Solicitud de permiso de notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        Log.d("FCM", "Permiso de notificaciones concedido")
                        subscribeToDailySignTopic()
                    } else {
                        Log.d("FCM", "Permiso de notificaciones denegado")
                    }
                }

            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Para Android <13 no se requiere permiso explícito
            subscribeToDailySignTopic()
        }

        setContent {
            OpenhandsTheme {
                AppNavigation()

                // ----------------------------
                // Composable de actualización automática
                var showDialog by remember { mutableStateOf(false) }
                var updateConfig by remember { mutableStateOf<AppUpdateConfig?>(null) }

                LaunchedEffect(Unit) {
                    //token de firebase
                    FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                        Log.d("FCM", "TOKEN => $token")
                    }
                    // 🔹 Usar versión real de la app
                    val currentVersionCode = BuildConfig.VERSION_CODE
                    val config = AppUpdateManager.shouldShowUpdate(currentVersionCode)
                    if (config != null) {
                        updateConfig = config
                        showDialog = true

                        // 🔹 Mandar notificación de update automáticamente
                        showUpdateNotification(config)
                    }
                }

                updateConfig?.let { config ->
                    if (showDialog) {
                        AppUpdateManager.UpdateDialog(config) {
                            showDialog = false
                        }
                    }
                }
            }
        }

        // ----------------------------
        // Verificación de Firebase
        //testFirebaseConfig()
    }

    // ---------------------------- Suscribirse al topic de Firebase
    private fun subscribeToDailySignTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("daily_sign")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Suscrito al topic daily_sign")
                } else {
                    Log.e("FCM", "Error al suscribirse al topic", task.exception)
                }
            }
    }

    // ---------------------------- Notificación automática de actualización
    private fun showUpdateNotification(config: AppUpdateConfig) {
        val channelId = "update_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Actualizaciones",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setContentTitle("Nueva actualización disponible")
            .setContentText("Versión ${config.latestVersionCode}: toca para ver novedades")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    // ---------------------------- Prueba de Firebase
/*    private fun testFirebaseConfig() {
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
    }*/
}
