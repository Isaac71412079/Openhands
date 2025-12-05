package com.example.openhands.features.timeprovider.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun TimeScreen(
    viewModel: TimeViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val timeState by viewModel.time.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTime()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Fecha y Hora Actual",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Tarjeta que muestra la hora
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hora desde servidor:",
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = timeState,
                        fontSize = 32.sp,
                        color = Color(0xFF152C58)
                    )
                }
            }
        }
    }
}

