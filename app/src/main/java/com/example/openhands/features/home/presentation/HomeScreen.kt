package com.example.openhands.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.SpeakerNotes
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.openhands.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTextActionClick: () -> Unit,
    onCameraActionClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Openhands", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.History, "Historial de Traducciones") },
                    label = { Text("Historial de Traducciones") },
                    selected = false,
                    onClick = { onHistoryClick(); scope.launch { drawerState.close() } }
                )
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Outlined.ManageAccounts, "Cambiar cuenta") },
                    label = { Text("Cambiar cuenta") },
                    selected = false,
                    onClick = onLogout // Llama a la misma función de logout
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar Sesión") },
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = onLogout
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Openhands", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Abrir menú", tint = Color.White)
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                modifier = Modifier.padding(bottom = 40.dp)
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
