package com.example.openhands.features.welcome.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.openhands.R

@Composable
fun WelcomeScreen(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF867AD2),
                        Color(0xFF152C58)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo de Openhands",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bienvenido...",
                style = MaterialTheme.typography.displaySmall, // Un tamaño grande y llamativo
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Traductor bidireccional de Lengua de Señas Boliviana (LSB). Aprende, comunica y conecta con facilidad.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Espacio más grande antes de los botones
            Spacer(modifier = Modifier.height(48.dp))

            // 4. Botón "Iniciar Sesión"
            Button(
                onClick = onLoginClicked, // Llama a la función de navegación
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F9EBB),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Iniciar Sesión",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Botón "Registrarse" (estilo diferente)
            OutlinedButton(
                onClick = onRegisterClicked, // Hará nada por ahora
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                border = BorderStroke(1.dp, Color.White) // Borde blanco
            ) {
                Text(
                    "Registrarse",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Texto blanco
                )
            }
        }
    }
}