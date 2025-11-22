package com.example.openhands.features.signcamera.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CapturedImageScreen(
    imageUri: String?,
    onRetakePhoto: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onRetakePhoto) {
                Text("Reintentar")
            }
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        }
    }
}
