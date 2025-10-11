package com.example.openhands.features.home.presentation

// IMPORTS (sin cambios)
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border // <-- NUEVO IMPORT para el borde
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.openhands.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onTextActionClick: () -> Unit,
    onImageActionClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
    ) {
        Image(
            painter = painterResource(id = R.drawable.openhands),
            contentDescription = "Logo de OpenHands",
            modifier = Modifier
                .align(Alignment.TopStart)
                // CAMBIO: Aumentamos el padding para bajar el logo y darle más aire.
                .padding(29.dp)
                .size(63.dp)
                .clip(CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = "Seleccione Una Accion:",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                // CAMBIO: Aumentamos el padding inferior para dar más espacio.
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // --- BOTÓN DE TEXTO ---
            val textInteractionSource = remember { MutableInteractionSource() }
            val isTextHovered by textInteractionSource.collectIsHoveredAsState()
            val textScale by animateFloatAsState(
                targetValue = if (isTextHovered) 1.1f else 1.0f,
                animationSpec = tween(durationMillis = 200)
            )

            // CAMBIO: Aumentamos el radio de las esquinas para un look más suave.
            val buttonShape = RoundedCornerShape(24.dp)

            Box(
                modifier = Modifier
                    .scale(textScale)
                    // CAMBIO: Aumentamos el tamaño del botón.
                    .size(180.dp)
                    // CAMBIO: Añadimos el borde decorativo.
                    .border(
                        width = 2.dp,
                        color = Color(0xFF33A1C9), // Un color azul claro similar al del logo
                        shape = buttonShape
                    )
                    .clip(buttonShape)
                    .background(Color(0xFF2C4A7E))
                    .clickable(onClick = onTextActionClick)
                    .hoverable(interactionSource = textInteractionSource),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.text),
                    contentDescription = "Acción de texto",
                    // CAMBIO: Aumentamos el tamaño del icono.
                    modifier = Modifier.size(100.dp)
                )
            }

            // CAMBIO: Aumentamos la altura del espaciador.
            Spacer(modifier = Modifier.height(40.dp))

            // --- BOTÓN DE IMAGEN ---
            val imageInteractionSource = remember { MutableInteractionSource() }
            val isImageHovered by imageInteractionSource.collectIsHoveredAsState()
            val imageScale by animateFloatAsState(
                targetValue = if (isImageHovered) 1.1f else 1.0f,
                animationSpec = tween(durationMillis = 200)
            )
            Box(
                modifier = Modifier
                    .scale(imageScale)
                    // CAMBIO: Aumentamos el tamaño del botón.
                    .size(180.dp)
                    // CAMBIO: Añadimos el borde decorativo.
                    .border(
                        width = 2.dp,
                        color = Color(0xFF33A1C9),
                        shape = buttonShape
                    )
                    .clip(buttonShape)
                    .background(Color(0xFF2C4A7E))
                    .clickable(onClick = onImageActionClick)
                    .hoverable(interactionSource = imageInteractionSource),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Acción de imagen",
                    // CAMBIO: Aumentamos el tamaño del icono.
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}