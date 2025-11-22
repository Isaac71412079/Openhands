package com.example.openhands.features.textsign.presentation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoResId: Int // Recibirá el ID del recurso, ej: R.raw.letra_a
) {
    val context = LocalContext.current

    // 1. Recordamos una instancia de ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // 2. Preparamos el video para reproducir
            val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()

            // 3. Configuraciones clave
            playWhenReady = true // Inicia la reproducción automáticamente
            volume = 0f          // Silenciamos el video
            repeatMode = Player.REPEAT_MODE_ONE // Reproducir en bucle infinito
        }
    }

    // 4. Gestionamos el ciclo de vida del player
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release() // Liberar recursos cuando el Composable se destruye
        }
    }

    // 5. Usamos AndroidView para incrustar el PlayerView de ExoPlayer
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false // Ocultamos los controles de reproducción
            }
        }
    )
}
