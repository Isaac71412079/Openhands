package com.example.openhands.features.home.presentation

// --- IMPORTS NECESARIOS ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openhands.R
import com.example.openhands.features.home.data.model.TranslationHistoryItem
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- COMPOSABLE PRINCIPAL DE LA PANTALLA ---
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onTextActionClick: () -> Unit,
    onImageActionClick: () -> Unit
) {
    val historyItems by viewModel.historyState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- SECCIÓN SUPERIOR CON LOGO Y BOTONES ---
        Image(
            painter = painterResource(id = R.drawable.openhands),
            contentDescription = "Logo de OpenHands",
            modifier = Modifier
                .padding(top = 29.dp)
                .size(63.dp)
                .clip(CircleShape)
        )
        Text(
            text = "Seleccione Una Accion:",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                iconResId = R.drawable.text,
                contentDescription = "Acción de texto",
                onClick = onTextActionClick
            )
            ActionButton(
                iconResId = R.drawable.camera,
                contentDescription = "Acción de imagen",
                onClick = onImageActionClick
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Historial Reciente",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (historyItems.isEmpty()) {
                // ESTADO VACÍO MEJORADO
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.HourglassEmpty,
                        contentDescription = "Historial vacío",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Aún no hay traducciones.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(historyItems) { item ->
                        HistoryItemCard(
                            item = item,
                            onClick = {
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: TranslationHistoryItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C4A7E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "Icono de historial",
                tint = Color(0xFF33A1C9),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.originalText,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatTimestamp(item.timestamp),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
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
            .size(150.dp)
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
            modifier = Modifier.size(80.dp)
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {

    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val netDate = Date(timestamp)
    return sdf.format(netDate)
}