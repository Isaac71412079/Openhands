package com.example.openhands.features.home.presentation

// IMPORTS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openhands.R
import org.koin.androidx.compose.koinViewModel

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

        // --- SECCIÓN INFERIOR CON HISTORIAL ---
        Text(
            text = "Historial Reciente",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa el espacio restante
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2C4A7E))
        ) {
            if (historyItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no hay traducciones.",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                items(historyItems) { item ->
                    Column {
                        Text(
                            text = item.originalText,
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        Divider(color = Color(0xFF152C58), thickness = 1.dp)
                    }
                }
            }
        }
    }
}

// Composable de ayuda para no repetir el código de los botones
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