
package com.example.openhands.features.signcamera.presentation
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors
import androidx.compose.ui.unit.sp // Import para el tamaño de fuente
// --- CONFIGURACIÓN DE OPTIMIZACIÓN ---
const val JPEG_QUALITY = 60 // <--- OPTIMIZACIÓN: Reducción de calidad de imagen para acelerar el envío
// ------------------------------------
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

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionState.status.isGranted) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
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

        // --- UI superpuesta SIMPLIFICADA ---
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. TOP BAR
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
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 2. CAJA DE TEXTO INFERIOR (SIMPLE)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = viewModel.detectedText,
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = viewModel::clearText) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Borrar Texto",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
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
        // --- CAMBIO CLAVE: CÁMARA TRASERA PARA MEJOR CALIDAD DE IMAGEN ---
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
// FUNCIONES DE UTILIDAD (Conversión de imagen)
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
// --- OPTIMIZACIÓN: Calidad de compresión reducida ---
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