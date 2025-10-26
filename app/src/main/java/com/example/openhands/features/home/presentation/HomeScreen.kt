package com.example.openhands.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.openhands.R

@Composable
fun HomeScreen(

    onTextActionClick: () -> Unit,
    onImageActionClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = onHistoryClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "Ver historial",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centra todo verticalmente
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo de OpenHands",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Seleccione Una Accion:",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            ActionButton(
                iconResId = R.drawable.text,
                contentDescription = "Acción de texto",
                onClick = onTextActionClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            ActionButton(
                iconResId = R.drawable.camera,
                contentDescription = "Acción de imagen",
                onClick = onImageActionClick
            )
        }
    }
}



@Composable
fun ActionButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier
            .size(180.dp)
            .border(
                width = 2.dp,
                color = Color(0xFF33A1C9),
                shape = buttonShape
            )
            .clip(buttonShape)
            .background(Color(0xFF2C4A7E))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(100.dp)
        )
    }
}