package com.example.openhands.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openhands.features.home.data.model.TranslationHistoryItem
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val historyItems by viewModel.historyState.collectAsState()

    // Fondo degradado coherente con la app
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF152C58), Color(0xFF0F1E3B))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Historial",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
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
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Botón opcional para limpiar todo (puedes conectarlo al ViewModel si quieres)
                    if (historyItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.logout() /* O llamar a clearHistory */ }) {
                            Icon(
                                Icons.Outlined.DeleteSweep,
                                contentDescription = "Borrar todo",
                                tint = Color(0xFFFF6B6B) // Rojo suave
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent // Para ver el gradiente del Box
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(paddingValues)
        ) {
            if (historyItems.isEmpty()) {
                EmptyHistoryState()
            } else {
                // Lista con animación
                LazyColumn(
                    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(historyItems) { index, item ->
                        AnimatedHistoryItem(item = item, index = index)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedHistoryItem(item: TranslationHistoryItem, index: Int) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Retraso escalonado para efecto cascada
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { 50 + (index * 20) } // Entra desde abajo
        ) + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        HistoryItemCard(item = item)
    }
}

@Composable
fun HistoryItemCard(item: TranslationHistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Acción al tocar */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            // Azul oscuro semitransparente para efecto moderno
            containerColor = Color(0xFF1F305E).copy(alpha = 0.8f)
        ),
        border = BorderStroke(1.dp, Color(0xFF33A1C9).copy(alpha = 0.3f)), // Borde cian sutil
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con fondo circular
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF33A1C9).copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = Color(0xFF33A1C9), // Cian de la marca
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.originalText,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pequeño indicador de tiempo
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatTimestamp(item.timestamp),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            // Botón de eliminar individual (Decorativo o funcional)
            IconButton(onClick = { /* Acción eliminar item */ }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White.copy(alpha = 0.3f), // Sutil
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Círculo decorativo de fondo
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.HourglassEmpty,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sin historial reciente",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tus traducciones aparecerán aquí",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 16.sp
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) // Formato más compacto
    val netDate = Date(timestamp)
    return sdf.format(netDate).replace(".", "")
}