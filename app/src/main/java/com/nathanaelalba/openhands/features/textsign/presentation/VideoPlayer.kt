// Reemplaza el contenido completo de tu archivo VideoPlayer.kt con este código
package com.nathanaelalba.openhands.features.textsign.presentation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoQueue: List<Int>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    DisposableEffect(videoQueue) {

        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED && videoQueue.size > 1) {
                    coroutineScope.launch {
                        delay(1100)
                        exoPlayer.seekTo(0, 0L)
                        exoPlayer.playWhenReady = true
                    }
                }
            }
        }
        exoPlayer.addListener(listener)

        val mediaItems = videoQueue.map { videoResId ->
            val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
            MediaItem.fromUri(uri)
        }

        if (mediaItems.isNotEmpty()) {
            exoPlayer.setMediaItems(mediaItems)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            exoPlayer.volume = 0f

            exoPlayer.repeatMode = if (mediaItems.size > 1) {
                Player.REPEAT_MODE_OFF
            } else {
                Player.REPEAT_MODE_ONE
            }
        } else {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
        }

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.stop()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
            }
        }
    )
}
