package com.nathanaelalba.openhands.features.signcamera.presentation

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

// --- CONFIGURACIÓN DE OPTIMIZACIÓN ---
const val JPEG_QUALITY = 60

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SignCameraScreen(
    viewModel: SignCameraViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearResources()
        }
    }

    Scaffold(
        containerColor = Color.Black, // Fondo negro para la cámara
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cameraPermissionState.status.isGranted) {
                // 1. VISTA DE CÁMARA (Fondo)
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel
                )

                // 2. CAPA DE GUIAS VISUALES (Overlay)
                ScanningOverlay()

            } else {
                PermissionDeniedContent()
            }

            // 3. INTERFAZ DE USUARIO (Top Bar y Bottom Panel)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(), // Respetar notch y barras
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // --- TOP BAR FLOTANTE ---
                TopBarControl(onNavigateBack)

                Spacer(modifier = Modifier.weight(1f))

                // --- PANEL INFERIOR DE RESULTADOS ---
                ResultPanel(
                    detectedText = viewModel.detectedText,
                    onClearClick = viewModel::clearText
                )
            }
        }
    }
}

@Composable
private fun TopBarControl(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón Atrás
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f)) // Semi-transparente
                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }

        // Indicador de Estado "En Vivo"
        Surface(
            shape = RoundedCornerShape(50),
            color = Color.Red.copy(alpha = 0.8f),
            modifier = Modifier.height(28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Default.FiberManualRecord,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "DETECTANDO",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ResultPanel(
    detectedText: String,
    onClearClick: () -> Unit
) {
    // Panel estilo "Bottom Sheet" con el color de la marca
    Surface(
        color = Color(0xFF152C58).copy(alpha = 0.95f), // Casi opaco para leer bien
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Traducción:",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF33A1C9) // Color acento Cian
                )

                // Botón Limpiar estilizado
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Limpiar",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Área de Texto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp) // Altura mínima para que no salte
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (detectedText.isEmpty() || detectedText == "...") {
                    Text(
                        text = "Realiza una seña frente a la cámara...",
                        color = Color.White.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                } else {
                    Text(
                        text = detectedText,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Dibuja un marco de enfoque para guiar al usuario
@Composable
private fun ScanningOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 4.dp.toPx()
        val cornerRadius = 24.dp.toPx()

        // Tamaño del cuadro de enfoque
        val boxWidth = size.width * 0.7f
        val boxHeight = size.height * 0.5f

        val left = (size.width - boxWidth) / 2
        val top = (size.height - boxHeight) / 2

        // Color del marco (Cian de la marca)
        val frameColor = Color(0xFF33A1C9)

        // Dibujar esquinas del marco (estilo visor)
        drawRoundRect(
            color = frameColor,
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
private fun PermissionDeniedContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Cameraswitch,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Se requiere acceso a la cámara",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// --- LÓGICA DE CÁMARA (Sin cambios funcionales, solo optimización visual) ---
@Composable
fun CameraPreview(modifier: Modifier = Modifier, viewModel: SignCameraViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    val analyzer = remember {
        ImageAnalysis.Analyzer { imageProxy ->
            val bitmap = imageProxy.toBitmapRobust()
            bitmap?.let {
                viewModel.sendFrame(it)
            }
            imageProxy.close()
        }
    }

    LaunchedEffect(cameraController) {
        cameraController.unbind()
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(analysisExecutor, analyzer)

        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.setImageAnalysisAnalyzer(analysisExecutor, analyzer)
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraController.clearImageAnalysisAnalyzer()
            analysisExecutor.shutdown()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                controller = cameraController
            }
        }
    )
}

fun ImageProxy.toBitmapRobust(): Bitmap? {
    if (format != ImageFormat.YUV_420_888) return null

    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, this.width, this.height), JPEG_QUALITY, out)
    val imageBytes = out.toByteArray()

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.let {
        rotateBitmap(it, imageInfo.rotationDegrees)
    }
}

fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
    if (rotationDegrees == 0) return bitmap
    val matrix = Matrix()
    matrix.postRotate(rotationDegrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}