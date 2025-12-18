package com.nathanaelalba.openhands.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nathanaelalba.openhands.features.settings.data.SettingsDataStore
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val themePreference by viewModel.themePreference.collectAsState()

    val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK

    val mainBackgroundBrush = if (useDarkTheme) {
        SolidColor(Color.Black)
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFF152C58), Color(0xFF0F2042)))
    }

    val topAppBarColor = Color.White

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
                // --- SECCIÓN DE APARIENCIA ---
                SettingsGroup(title = "Apariencia", useDarkTheme = useDarkTheme) {
                    SettingsItem(text = "Modo Oscuro", useDarkTheme = useDarkTheme) {
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

                Spacer(modifier = Modifier.height(24.dp))

                // --- SECCIÓN LEGAL ---
                SettingsGroup(title = "Legal", useDarkTheme = useDarkTheme) {
                    SettingsClickableItem(
                        text = "Política de Privacidad",
                        icon = Icons.Default.Policy,
                        useDarkTheme = useDarkTheme,
                        onClick = onPrivacyPolicyClick
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsGroup(title: String, useDarkTheme: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            // --- LÍNEA CORREGIDA ---
            color = Color.LightGray,
            shape = MaterialTheme.shapes.medium,
            content = {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    content()
                }
            }
        )
    }
}

@Composable
fun SettingsItem(text: String, useDarkTheme: Boolean, content: @Composable () -> Unit) {
    val itemTextColor = if (useDarkTheme) Color.Black else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = itemTextColor
        )
        content()
    }
}

@Composable
fun SettingsClickableItem(text: String, icon: ImageVector, useDarkTheme: Boolean, onClick: () -> Unit) {
    val itemTextColor = if (useDarkTheme) Color.Black else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = itemTextColor.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = itemTextColor,
            modifier = Modifier.weight(1f)
        )
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = itemTextColor.copy(alpha = 0.6f))
    }
}