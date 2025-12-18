package com.example.openhands.features.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.openhands.R
import com.example.openhands.features.settings.data.SettingsDataStore
import com.example.openhands.features.settings.presentation.SettingsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    var startAnimation by remember { mutableStateOf(false) }
    val themePreference by settingsViewModel.themePreference.collectAsState()

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    // --- Lógica de Tema ---
    val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK
    val backgroundBrush = if (useDarkTheme) SolidColor(Color.Black) else SolidColor(Color(0xFF152C58))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StaggeredAnimate(visible = startAnimation, index = 0) {
                Image(
                    painter = painterResource(id = R.drawable.openhands),
                    contentDescription = "Logo Openhands",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            RegisterContent(viewModel, onRegisterSuccess, startAnimation, useDarkTheme)
        }

        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver atrás",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun RegisterContent(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    startAnimation: Boolean,
    useDarkTheme: Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) showSuccessDialog = true
    }

    LaunchedEffect(showSuccessDialog) {
        if (!showSuccessDialog && uiState.success) {
            delay(200)
            onRegisterSuccess()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(max = 380.dp)
    ) {
        StaggeredAnimate(visible = startAnimation, index = 1) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Únete a la comunidad OpenHands",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        StaggeredAnimate(visible = startAnimation, index = 2) {
            RegisterTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                useDarkTheme = useDarkTheme
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAnimate(visible = startAnimation, index = 3) {
            RegisterTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                icon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                useDarkTheme = useDarkTheme
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StaggeredAnimate(visible = startAnimation, index = 4) {
            RegisterTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                icon = Icons.Default.LockReset,
                keyboardType = KeyboardType.Password,
                isError = uiState.confirmPasswordError != null,
                errorMessage = uiState.confirmPasswordError,
                isPassword = true,
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                useDarkTheme = useDarkTheme
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        StaggeredAnimate(visible = startAnimation, index = 5) {
            val buttonBrush = Brush.horizontalGradient(
                colors = listOf(Color(0xFF1F9EBB), Color(0xFF33A1C9))
            )

            Button(
                onClick = { viewModel.registerUser(email, password, confirmPassword) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(buttonBrush, RoundedCornerShape(50)),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                enabled = !uiState.isLoading
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(text = "Registrarse", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        uiState.genericError?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, style = MaterialTheme.typography.bodySmall, color = Color(0xFFFF6B6B), modifier = Modifier.background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(8.dp))
        }

        if (showSuccessDialog) {
            RegisterSuccessDialog(onDismiss = { showSuccessDialog = false }, useDarkTheme = useDarkTheme)
        }
    }
}

@Composable
private fun StaggeredAnimate(
    visible: Boolean,
    index: Int,
    content: @Composable () -> Unit
) {
    val delay = index * 80
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { 150 } 
        ) + fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
    ) {
        content()
    }
}

@Composable
private fun RegisterTextField(
    value: String, 
    onValueChange: (String) -> Unit, 
    label: String, 
    icon: ImageVector, 
    keyboardType: KeyboardType, 
    isError: Boolean, 
    errorMessage: String?, 
    isPassword: Boolean = false, 
    passwordVisible: Boolean = false, 
    onPasswordVisibilityChange: () -> Unit = {},
    useDarkTheme: Boolean
) {
    val errorColor = Color(0xFFFF6B6B)
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.7f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            textStyle = TextStyle(color = Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isError,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) { { IconButton(onClick = onPasswordVisibilityChange) { Icon(if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = Color.White.copy(alpha = 0.7f)) } } } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF33A1C9), 
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f), 
                focusedLabelColor = Color(0xFF33A1C9), 
                unfocusedLabelColor = Color.White.copy(alpha = 0.5f), 
                cursorColor = Color.White, 
                errorBorderColor = errorColor, 
                errorLabelColor = errorColor, 
                focusedContainerColor = if (useDarkTheme) Color.Gray.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = if (useDarkTheme) Color.Gray.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f)
            )
        )
        if (errorMessage != null) { Text(text = errorMessage, color = errorColor, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp, top = 4.dp)) }
    }
}

@Composable
private fun RegisterSuccessDialog(onDismiss: () -> Unit, useDarkTheme: Boolean) {
    val successGradientBrush = Brush.linearGradient(colors = listOf(Color(0xFF33A1C9), Color(0xFF1F9EBB)))
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(28.dp), 
            color = if (useDarkTheme) Color.DarkGray else Color(0xFF152C58), 
            border = BorderStroke(1.5.dp, Color(0xFF33A1C9).copy(alpha = 0.5f)), 
            shadowElevation = 10.dp) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(80.dp).graphicsLayer(alpha = 0.99f).drawWithCache { onDrawWithContent { drawContent(); drawRect(brush = successGradientBrush, blendMode = BlendMode.SrcAtop) } })
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "¡Cuenta Creada!", style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tu registro se completó con éxito", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f)), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
    LaunchedEffect(Unit) { delay(1500L); onDismiss() }
}