package com.example.openhands.features.textsign.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openhands.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSignScreen(
    viewModel: TextSignViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToMoreLanguages: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // Fondo con degradado sutil para dar profundidad
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF152C58), Color(0xFF0F1E3B))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar( // Título centrado se ve mejor
                title = {
                    Text(
                        "Traductor LSB",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Logo pequeño y sutil
                    Image(
                        painter = painterResource(id = R.drawable.openhands),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent, // Importante para ver el degradado del Box
        bottomBar = {
            BottomInputArea(viewModel, keyboardController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp), // Padding lateral más limpio
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Subtítulo descriptivo
                Text(
                    text = "Escribe texto para visualizar la seña",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- CONTENEDOR DE VIDEO (Estilo Pantalla de Cine) ---
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp), // Un poco más alto para mejor vista
                    shape = RoundedCornerShape(32.dp), // Bordes muy redondeados
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val currentVideoQueue = viewModel.videoQueue
                        if (currentVideoQueue.isEmpty()) {
                            VideoPlaceholder()
                        } else {
                            VideoPlayer(
                                modifier = Modifier.fillMaxSize(),
                                videoQueue = currentVideoQueue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- BOTÓN MÁS IDIOMAS (Estilo Píldora Elegante) ---
                OutlinedButton(
                    onClick = onNavigateToMoreLanguages,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFF0E8FF).copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFF0E8FF)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Explorar más idiomas en Sign.mt",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Espacio final
            }
        }
    }
}

@Composable
private fun BottomInputArea(viewModel: TextSignViewModel, keyboardController: Any?) {
    // Panel inferior con un color ligeramente diferente para separarlo visualmente
    Surface(
        color = Color(0xFF0F1E3B), // Un tono más oscuro que el fondo
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(), // Respetar gestos del sistema
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Input tipo "Chat"
            TextField(
                value = viewModel.inputText,
                onValueChange = viewModel::onTextChanged,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), // Altura cómoda
                placeholder = {
                    Text(
                        "Escribe aquí...",
                        color = Color.Gray
                    )
                },
                shape = RoundedCornerShape(50), // Completamente redondo
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFF152C58),
                    // Quitar la línea de abajo fea por defecto
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Botón de Enviar Flotante
            FloatingActionButton(
                onClick = {
                    viewModel.onTranslateClicked()
                    (keyboardController as? androidx.compose.ui.platform.SoftwareKeyboardController)?.hide()
                },
                containerColor = Color(0xFFF0E8FF), // Color de acento claro
                contentColor = Color(0xFF152C58), // Icono oscuro
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Traducir",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun VideoPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2C4A7E), Color(0xFF000000))
                )
            )
    ) {
        // Círculo decorativo detrás del icono
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayCircleFilled,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFFF0E8FF).copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esperando texto...",
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "El video de la seña aparecerá aquí",
            color = Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}