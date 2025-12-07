package com.example.openhands.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.SpeakerNotes
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.openhands.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userEmail: String,
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerBackgroundColor = Color(0xFFF0E8FF)
    val drawerContentColor = Color(0xFF152C58)

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Transparent,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = drawerBackgroundColor,
                drawerContentColor = drawerContentColor
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.close() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Cerrar menú",
                                tint = drawerContentColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Openhands",
                            style = MaterialTheme.typography.titleLarge,
                            color = drawerContentColor,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.openhands),
                        contentDescription = "Logo de Openhands",
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = drawerContentColor.copy(alpha = 0.2f))

                    Text(
                        text = userEmail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = drawerContentColor.copy(alpha = 0.7f),
                        maxLines = 1
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = drawerContentColor.copy(alpha = 0.2f))

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val navDrawerItemColors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = drawerContentColor,
                            unselectedTextColor = drawerContentColor
                        )
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Outlined.History, "Historial de Traducciones") },
                            label = { Text("Historial de Traducciones") },
                            selected = false,
                            onClick = { onHistoryClick(); scope.launch { drawerState.close() } },
                            colors = navDrawerItemColors
                        )
                        NavigationDrawerItem(
                            icon = { Icon(Icons.AutoMirrored.Filled.SpeakerNotes, "Texto a Señas") },
                            label = { Text("Texto a Señas") },
                            selected = false,
                            onClick = { onTextActionClick(); scope.launch { drawerState.close() } },
                            colors = navDrawerItemColors
                        )
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Outlined.PhotoCamera, "Señas a Texto") },
                            label = { Text("Señas a Texto") },
                            selected = false,
                            onClick = { onCameraActionClick(); scope.launch { drawerState.close() } },
                            colors = navDrawerItemColors
                        )
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = drawerContentColor.copy(alpha = 0.2f))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val navDrawerItemColors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = drawerContentColor,
                            unselectedTextColor = drawerContentColor
                        )
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Outlined.ManageAccounts, "Cambiar cuenta") },
                            label = { Text("Cambiar cuenta") },
                            selected = false,
                            onClick = onLogout,
                            colors = navDrawerItemColors
                        )
                        NavigationDrawerItem(
                            icon = { Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar Sesión") },
                            label = { Text("Cerrar Sesión") },
                            selected = false,
                            onClick = onLogout,
                            colors = navDrawerItemColors
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        Row(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Menu, "Abrir/Cerrar menú",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Openhands", color = Color.White, style = MaterialTheme.typography.titleLarge)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF152C58))
                )
            }
        ) {
            HomeScreenContent(
                modifier = Modifier.padding(it),
                onTextActionClick = onTextActionClick,
                onCameraActionClick = onCameraActionClick
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
            .padding(16.dp)
    ) {
        if (maxWidth > maxHeight) {
            LandscapeHomeScreen(onTextActionClick, onCameraActionClick)
        } else {
            PortraitHomeScreen(onTextActionClick, onCameraActionClick)
        }
    }
}

@Composable
private fun PortraitHomeScreen(
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.openhands),
            contentDescription = "Logo de OpenHands",
            modifier = Modifier.size(120.dp).clip(RoundedCornerShape(24.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Seleccione Una Acción:",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 40.dp),
            fontWeight = FontWeight.Bold
        )
        ActionButton(
            iconResId = R.drawable.text,
            contentDescription = "Texto a Señas",
            onClick = onTextActionClick
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            iconResId = R.drawable.camera,
            contentDescription = "Señas a Texto",
            onClick = onCameraActionClick
        )
    }
}

@Composable
private fun LandscapeHomeScreen(
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // 1. Centrar todo el contenido
    ) {
        Text(
            text = "Seleccione Una Acción:",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier // 2. Quitar el padding para que se centre correctamente
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                iconResId = R.drawable.text,
                contentDescription = "Texto a Señas",
                onClick = onTextActionClick
            )
            ActionButton(
                iconResId = R.drawable.camera,
                contentDescription = "Señas a Texto",
                onClick = onCameraActionClick
            )
        }
    }
}

@Composable
private fun ActionButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    val buttonShape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier
            .size(180.dp)
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
            modifier = Modifier.size(100.dp)
        )
    }
}
