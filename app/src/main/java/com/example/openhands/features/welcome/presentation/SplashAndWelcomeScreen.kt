package com.example.openhands.features.welcome.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.openhands.R
import kotlinx.coroutines.delay

@Composable
fun SplashAndWelcomeScreen(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {

    var isSplashFinished by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(1500L)
        isSplashFinished = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58)),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints {
            if (maxWidth > maxHeight) {
                LandscapeWelcomeLayout(onLoginClicked, onRegisterClicked)
            } else {
                PortraitWelcomeLayout(isSplashFinished, onLoginClicked, onRegisterClicked)
            }
        }
    }
}

@Composable
private fun PortraitWelcomeLayout(
    isSplashFinished: Boolean,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    val logoSize by animateDpAsState(
        targetValue = if (isSplashFinished) 300.dp else 350.dp,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(35.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.openhands),
            contentDescription = "Logo de Openhands",
            modifier = Modifier.size(logoSize)
        )

        AnimatedVisibility(
            visible = isSplashFinished,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(animationSpec = tween(1000))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                WelcomeTexts()
                Spacer(modifier = Modifier.height(60.dp))
                AuthButtons(onLoginClicked, onRegisterClicked)
            }
        }
    }
}

@Composable
private fun LandscapeWelcomeLayout(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Lado izquierdo: SOLO el Logo
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo de Openhands",
                modifier = Modifier.size(280.dp)
            )
        }

        // Lado derecho: Botones y DEBAJO los textos
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthButtons(onLoginClicked, onRegisterClicked)
            Spacer(modifier = Modifier.height(24.dp))
            WelcomeTexts()
        }
    }
}

// Componente solo para los textos de bienvenida
@Composable
private fun WelcomeTexts() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bienvenido...",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Traductor bidireccional de Lengua de Señas Boliviana (LSB). Aprende, comunica y conecta con facilidad",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AuthButtons(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    Column(modifier = Modifier.width(340.dp)) {
        Button(
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth().height(57.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF0E8FF),
                contentColor = Color(0xFF152C58)
            )
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onRegisterClicked,
            modifier = Modifier.fillMaxWidth().height(57.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF0E8FF),
                contentColor = Color(0xFF152C58)
            )
        ) {
            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
