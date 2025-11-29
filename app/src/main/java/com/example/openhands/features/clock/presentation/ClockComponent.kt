package com.example.openhands.features.clock.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Esta es la función que usarás en tus pantallas
@Composable
fun RealTimeClock(
    viewModel: ClockViewModel,
    modifier: Modifier = Modifier
) {
    val timeString by viewModel.timeState.collectAsState()

    // En ClockComponent.kt
    Text(
        text = timeString,
        modifier = modifier,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White // <--- CAMBIAR ESTO A WHITE (antes era Black)
    )
}