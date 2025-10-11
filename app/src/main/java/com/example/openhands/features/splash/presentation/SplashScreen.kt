package com.example.openhands.features.splash.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.openhands.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Dos animaciones para un efecto más suave: una para la escala y otra para la opacidad (fade-in).
    val scale = remember { Animatable(0.5f) } // Empezamos desde la mitad del tamaño
    val alpha = remember { Animatable(0f) }   // Empezamos completamente transparente

    // Un solo LaunchedEffect para controlar toda la lógica y evitar conflictos.
    LaunchedEffect(key1 = true) {
        // Ejecutamos ambas animaciones al mismo tiempo (concurrentemente)
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200) // Animación más larga y suave
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200)
            )
        }

        // Esperamos un total de 2.5 segundos.
        // Esto le da tiempo a la animación de completarse y al usuario de ver el logo.
        delay(2500L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // CAMBIO CLAVE: Usamos el mismo fondo azul sólido que en WelcomeScreen.
            // Esto elimina el "salto" de color y hace la transición invisible.
            .background(Color(0xFF152C58)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.openhands),
            contentDescription = "Logo de Openhands",
            modifier = Modifier
                .size(350.dp)
                .scale(scale.value) // Aplicamos la animación de escala
                .alpha(alpha.value) // Aplicamos la animación de opacidad
        )
    }
}