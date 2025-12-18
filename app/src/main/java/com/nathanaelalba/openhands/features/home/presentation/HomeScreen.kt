package com.nathanaelalba.openhands.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nathanaelalba.openhands.R
import com.nathanaelalba.openhands.features.settings.data.SettingsDataStore
import com.nathanaelalba.openhands.features.settings.presentation.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userEmail: String,
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit, 
    onLogout: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val themePreference by settingsViewModel.themePreference.collectAsState()

    // --- Lógica de Tema Corregida ---
    val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK

    val mainBackgroundBrush = if (useDarkTheme) {
        SolidColor(Color.Black)
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFF152C58), Color(0xFF0F2042)))
    }

    val drawerHeaderBrush = if (useDarkTheme) {
        SolidColor(Color.DarkGray)
    } else {
        Brush.linearGradient(colors = listOf(Color(0xFF152C58), Color(0xFF2C4A7E)))
    }
    
    val drawerBackgroundColor = if (useDarkTheme) Color(0xFF121212) else Color(0xFFF9F9FF)
    val drawerContentColor = if (useDarkTheme) Color.White else Color(0xFF152C58)
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = drawerBackgroundColor,
                drawerContentColor = drawerContentColor,
                modifier = Modifier.width(300.dp)
            ) {
                // --- HEADER DEL DRAWER ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(drawerHeaderBrush),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.openhands),
                                contentDescription = "Logo",
                                modifier = Modifier.padding(8.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "OpenHands",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 1
                        )
                    }
                }

                // --- ITEMS DEL MENU ---
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                ) {
                    DrawerItem(
                        icon = Icons.Outlined.History,
                        label = "Historial de Traducciones",
                        onClick = { onHistoryClick(); scope.launch { drawerState.close() } },
                        isDestructive = false,
                        contentColor = drawerContentColor
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.2f))

                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                    )

                    DrawerItem(
                        icon = Icons.Outlined.Settings,
                        label = "Ajustes",
                        onClick = { onSettingsClick(); scope.launch { drawerState.close() } },
                        isDestructive = false,
                        contentColor = drawerContentColor
                    )

                    DrawerItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        label = "Cerrar Sesión",
                        onClick = onLogout,
                        isDestructive = true,
                        contentColor = drawerContentColor 
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Menu, "Menú",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
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
                HomeScreenContent(
                    userEmail = userEmail,
                    onTextActionClick = onTextActionClick,
                    onCameraActionClick = onCameraActionClick
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    contentColor: Color
) {
    val color = if (isDestructive) Color(0xFFFF6B6B) else contentColor

    NavigationDrawerItem(
        icon = { Icon(icon, null, tint = color) },
        label = { Text(label, fontWeight = if(isDestructive) FontWeight.Bold else FontWeight.Normal) },
        selected = false,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            unselectedTextColor = color
        ),
        modifier = Modifier.padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun HomeScreenContent(
    userEmail: String,
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit
) {
    val userName = remember(userEmail) {
        userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- SALUDO ---
        Text(
            text = "Hola, $userName",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "¿Qué deseas traducir hoy?",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // --- TARJETAS ---
        ActionCard(
            title = "Texto a Señas",
            subtitle = "Escribe y traduce a LSB",
            iconResId = R.drawable.text,
            gradientColors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)),
            onClick = onTextActionClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        ActionCard(
            title = "Señas a Texto",
            subtitle = "Usa la cámara para traducir",
            iconResId = R.drawable.camera,
            gradientColors = listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
            onClick = onCameraActionClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    iconResId: Int,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()

            .heightIn(min = 160.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradientColors))
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 20.dp, y = (-20).dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .align(Alignment.TopEnd)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "COMENZAR",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer { rotationZ = 10f }
                )
            }
        }
    }
}