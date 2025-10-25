package com.example.openhands.features.signcamera.presentation

import android.Manifest
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignCameraScreen(
    viewModel: SignCameraViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val capturedBitmap = viewModel.capturedImageBitmap

    // Creamos y recordamos el controlador de la cámara aquí para poder usarlo en toda la pantalla
    val cameraController = remember { LifecycleCameraController(context) }

    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            if (capturedBitmap == null) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    cameraController = cameraController
                )
            } else {
                Image(
                    bitmap = capturedBitmap.asImageBitmap(),
                    contentDescription = "Foto capturada",
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            PermissionDeniedMessage()
        }

        // UI SUPERPUESTA (CON BOTONES DINÁMICOS)
        OverlayUI(
            isPhotoCaptured = capturedBitmap != null,
            detectedText = viewModel.detectedText,
            onNavigateBack = onNavigateBack,
            onTakePhoto = {
                val executor = ContextCompat.getMainExecutor(context)
                viewModel.onTakePhotoClicked(cameraController, executor)
            },
            onRetakePhoto = viewModel::onRetakePhotoClicked,
            onConfirmPhoto = viewModel::onConfirmPhotoClicked
        )
    }
}


@Composable
private fun OverlayUI(
    isPhotoCaptured: Boolean,
    detectedText: String,
    onNavigateBack: () -> Unit,
    onTakePhoto: () -> Unit,
    onRetakePhoto: () -> Unit,
    onConfirmPhoto: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.clip(CircleShape).background(Color(0xFF152C58))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Recuadro de detección (solo visible en modo preview)
        if (!isPhotoCaptured) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp) // Centrado
                    .aspectRatio(3f / 4f) // Proporción 3:4
                    .border(width = 4.dp, color = Color.Yellow)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Caja de texto inferior con la traducción
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = detectedText, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
        }

        // Botones de acción inferiores
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (isPhotoCaptured) {
                // Botones para Reintentar y Confirmar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onRetakePhoto, modifier = Modifier.size(64.dp)) {
                        Icon(Icons.Default.Replay, "Reintentar", tint = Color.White, modifier = Modifier.fillMaxSize())
                    }
                    IconButton(onClick = onConfirmPhoto, modifier = Modifier.size(64.dp)) {
                        Icon(Icons.Default.Check, "Confirmar", tint = Color.Green, modifier = Modifier.fillMaxSize())
                    }
                }
            } else {
                // Botón para tomar la foto
                IconButton(
                    onClick = onTakePhoto,
                    modifier = Modifier.padding(vertical = 24.dp).size(72.dp)
                ) {
                    Icon(Icons.Default.Camera, "Tomar foto", tint = Color.White, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

// MODIFICADO: Ahora acepta el controlador como parámetro
@Composable
fun CameraPreview(modifier: Modifier = Modifier, cameraController: LifecycleCameraController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        }
    )
}

@Composable
fun PermissionDeniedMessage() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF152C58)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Se requiere permiso de cámara para continuar.",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}