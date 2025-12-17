package com.example.openhands.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.openhands.MainActivity
import com.example.openhands.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 1. Caso: Notificación con Datos ("La Seña del Día")
        // Si el mensaje trae datos ocultos (payload), los procesamos.
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "OpenHands"
            val body = remoteMessage.data["body"] ?: "Nueva actualización"
            val wordOfTheDay = remoteMessage.data["word"] // Ej: "GRACIAS"

            showNotification(title, body, wordOfTheDay)
        }
        // 2. Caso: Notificación Simple (Anuncios desde Consola)
        else if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage.notification?.title,
                remoteMessage.notification?.body,
                null
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aquí podrías enviar el token a tu servidor si quisieras guardar usuarios específicos.
    }

    private fun showNotification(title: String?, message: String?, wordOfTheDay: String?) {
        val channelId = "openhands_daily_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal para Android 8+ (Obligatorio)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "La Seña del Día y Novedades",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Configurar qué pasa al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Si hay una palabra del día, la pasamos al MainActivity
            if (wordOfTheDay != null) {
                putExtra("WORD_OF_THE_DAY", wordOfTheDay)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.openhands) // Asegúrate de que este recurso exista y sea válido para notificaciones
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}