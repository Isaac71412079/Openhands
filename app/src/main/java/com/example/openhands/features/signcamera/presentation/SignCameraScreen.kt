package com.example.openhands.features.signcamera.presentation

import android.Manifest
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.openhands.navigation.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignCameraScreen(
    rootNavController: NavController,
    viewModel: SignCameraViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                cameraController = cameraController
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF152C58)),
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

        Column(modifier = Modifier.fillMaxSize()) {
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
                        .clip(CircleShape)
                        .background(Color(0xFF152C58))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            // Recuadro de detección
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(180.dp)
                    .height(240.dp)
                    .border(width = 4.dp, color = Color.Yellow)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Texto de instrucción
            Text(
                text = "Centre las manos dentro del recuadro",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Botón de captura
        IconButton(
            onClick = {
                val photoFile = File(context.cacheDir, "captured_image.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                cameraController.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            exc.printStackTrace()
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                            println("URI capturada: $savedUri") // para depuración
                            viewModel.onImageCaptured(savedUri.toString())

                            // Navegar a la pantalla de CapturedImage sin pasar la URI
                            rootNavController.navigate(Screen.CapturedImage.route)
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Tomar foto",
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController
) {
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