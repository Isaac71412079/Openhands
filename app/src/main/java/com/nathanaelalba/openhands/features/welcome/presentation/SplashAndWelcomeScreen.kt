package com.nathanaelalba.openhands.features.welcome.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nathanaelalba.openhands.R
import com.nathanaelalba.openhands.features.privacy_policy.PrivacyPolicyScreen
import com.nathanaelalba.openhands.features.privacy_policy.viewmodel.PrivacyPolicyViewModel

import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

import com.nathanaelalba.openhands.features.welcome.viewmodel.RemoteViewModel
import com.nathanaelalba.openhands.features.settings.data.SettingsDataStore
import com.nathanaelalba.openhands.features.settings.presentation.SettingsViewModel

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.messaging.FirebaseMessaging


@Composable
fun SplashAndWelcomeScreen(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val privacyPolicyViewModel: PrivacyPolicyViewModel = koinViewModel()
    val hasAcceptedPrivacyPolicy by privacyPolicyViewModel.hasAcceptedPrivacyPolicy.collectAsState()
    val themePreference by settingsViewModel.themePreference.collectAsState()

    // Estado para controlar el inicio de la animación
    var startAnimation by remember { mutableStateOf(false) }

    // Uso de notificaciones de appupdate
    val remoteViewModel: RemoteViewModel = koinViewModel()
    val welcomeMessage by remoteViewModel.welcomeMessage.collectAsState()

    // Permiso de notificaciones
    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            // Por ahora no hacemos nada con el resultado
        }

    // Iniciar animación
    LaunchedEffect(key1 = true) {
        delay(1000)
        startAnimation = true
    }

    // --- Suscripción a topic y permiso, solo si aceptó la política ---
    LaunchedEffect(hasAcceptedPrivacyPolicy) {
        if (hasAcceptedPrivacyPolicy) {
            // Pedir permiso solo en Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
            // Suscribirse al topic "daily_sign"
            FirebaseMessaging.getInstance().subscribeToTopic("daily_sign")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "Suscrito al topic Daily Sign")
                    }
                }
        }
    }

    // --- Lógica de Tema ---
    val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK
    val backgroundBrush = if (useDarkTheme) {
        SolidColor(Color.Black)
    } else {
        SolidColor(Color(0xFF152C58))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints {
            val screenHeight = maxHeight
            if (maxWidth > maxHeight) {
                LandscapeLayout(
                    expanded = startAnimation,
                    welcomeMessage = welcomeMessage,
                    onLoginClicked = onLoginClicked,
                    onRegisterClicked = onRegisterClicked,
                    useDarkTheme = useDarkTheme
                )
            } else {
                PortraitLayout(
                    screenHeight = screenHeight,
                    expanded = startAnimation,
                    welcomeMessage = welcomeMessage,
                    onLoginClicked = onLoginClicked,
                    onRegisterClicked = onRegisterClicked,
                    useDarkTheme = useDarkTheme
                )
            }

            // Lógica de Política de Privacidad
            if (startAnimation && !hasAcceptedPrivacyPolicy) {
                var showPolicy by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(900)
                    showPolicy = true
                }
                if (showPolicy) {
                    PrivacyPolicyScreen(
                        onContinueClicked = {
                            privacyPolicyViewModel.acceptPrivacyPolicy()
                            // NOTIFICACIONES ahora se manejan automáticamente en LaunchedEffect
                        }
                    )
                }
            }

                )
            }
        }
    }
}

@Composable
private fun PortraitLayout(
    screenHeight: Dp,
    expanded: Boolean, // "expanded" significa que estamos en modo Bienvenida (Logo arriba)
    welcomeMessage: String,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    useDarkTheme: Boolean
) {
    val animDuration = 800
    val animEasing = FastOutSlowInEasing
    val initialLogoSize = 300.dp
    val finalLogoSize = 200.dp
    val centerPos = (screenHeight / 2) - (initialLogoSize / 2)
    val topPos = 60.dp 
    val logoSize by animateDpAsState(
        targetValue = if (expanded) finalLogoSize else initialLogoSize,
        animationSpec = tween(animDuration, easing = animEasing),
        label = "LogoSize"
    )

    val topSpacerHeight by animateDpAsState(
        targetValue = if (expanded) topPos else centerPos,
        animationSpec = tween(animDuration, easing = animEasing),
        label = "TopSpacer"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        label = "ContentAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(topSpacerHeight))

        Image(
            painter = painterResource(id = R.drawable.openhands),
            contentDescription = "Logo de Openhands",
            modifier = Modifier.size(logoSize),
            contentScale = ContentScale.Fit
        )

      Box(
          modifier = Modifier
              .fillMaxWidth()
              .graphicsLayer {
                  alpha = contentAlpha
                  translationY = if (expanded) 0f else 50f
              },
          contentAlignment = Alignment.Center
      ) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(top = 24.dp)
          ) {
              // Mostrar mensaje de bienvenida
              WelcomeTexts(welcomeMessage)

              Spacer(modifier = Modifier.height(48.dp))

              // Botones de autenticación
              AuthButtons(onLoginClicked, onRegisterClicked, useDarkTheme)

              Spacer(modifier = Modifier.height(40.dp))
          }
      }

            }
        }
    }
}

@Composable
private fun LandscapeLayout(
    expanded: Boolean,
    welcomeMessage: String,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    useDarkTheme: Boolean
) {
    val animDuration = 1000

    val logoSize by animateDpAsState(
        targetValue = if (expanded) 250.dp else 300.dp,
        animationSpec = tween(animDuration, easing = FastOutSlowInEasing),
        label = "LogoSizeLand"
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo",
                modifier = Modifier.size(logoSize)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(animDuration)) +
                    slideInVertically(
                        initialOffsetY = { 100 },
                        animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
                    )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {     
                AuthButtons(onLoginClicked, onRegisterClicked, useDarkTheme)
                Spacer(modifier = Modifier.height(24.dp))
                WelcomeTexts(welcomeMessage)
            }
        }
    }
}

@Composable
private fun WelcomeTexts(message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Bienvenido a Openhands",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AuthButtons(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    useDarkTheme: Boolean
) {
    val buttonColor = if (useDarkTheme) Color.DarkGray else Color(0xFFF0E8FF)
    val buttonTextColor = if (useDarkTheme) Color.White else Color(0xFF152C58)

    Column(modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth()) {
        Button(
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor
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
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor
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