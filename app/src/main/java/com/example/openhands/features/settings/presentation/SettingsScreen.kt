package com.example.openhands.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Nightlight // Icono de Luna
import androidx.compose.material.icons.filled.WbSunny // Icono de Sol
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.openhands.features.settings.data.SettingsDataStore
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val themePreference by viewModel.themePreference.collectAsState()

    val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK

    val mainBackgroundBrush = if (useDarkTheme) {
        SolidColor(Color.Black)
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFF152C58), Color(0xFF0F2042)))
    }
    
    // Colores de texto adaptativos
    val topAppBarColor = Color.White // Siempre blanco sobre fondo oscuro/azul
    val titleColor = if (useDarkTheme) Color(0xFFB0C4DE) else Color.White
    val subtitleColor = if (useDarkTheme) Color.LightGray else Color.White.copy(alpha = 0.8f)
    val itemTextColor = if (useDarkTheme) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", color = topAppBarColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Volver",
                            tint = topAppBarColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent 
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainBackgroundBrush) 
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Contenedor para la opción de tema
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = if(useDarkTheme) 0.1f else 1f),
                    shape = MaterialTheme.shapes.medium
                ) {
                     Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Modo Oscuro",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = itemTextColor
                        )

                        Switch(
                            checked = useDarkTheme,
                            onCheckedChange = {
                                val newTheme = if (it) SettingsDataStore.THEME_DARK else SettingsDataStore.THEME_SYSTEM
                                viewModel.setThemePreference(newTheme)
                            },
                            thumbContent = {
                                Icon(
                                    imageVector = if (useDarkTheme) Icons.Default.Nightlight else Icons.Default.WbSunny,
                                    contentDescription = "Icono de Tema",
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
