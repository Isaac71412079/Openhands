package com.example.openhands.features.login.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.openhands.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.Image

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    // Estado global de entrada
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ELEMENTO 1: LOGO (Entra primero)
            StaggeredAnimate(visible = startAnimation, index = 0) {
                Image(
                    painter = painterResource(id = R.drawable.openhands),
                    contentDescription = "Logo Openhands",
                    modifier = Modifier.size(130.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LoginContent(viewModel, onLoginSuccess, onRegisterClicked, startAnimation)
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
private fun LoginContent(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit,
    startAnimation: Boolean // Recibimos el estado de animación
) {
    var email by rememberSaveable { mutableStateOf(viewModel.email) }
    var password by rememberSaveable { mutableStateOf(viewModel.password) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val uiState = viewModel.uiState

    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) showSuccessDialog = true
    }

    LaunchedEffect(showSuccessDialog) {
        if (!showSuccessDialog && uiState.success) {
            delay(200)
            onLoginSuccess()
        }
    }

    LaunchedEffect(email) { viewModel.onEmailChange(email) }
    LaunchedEffect(password) { viewModel.onPasswordChange(password) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(max = 380.dp)
    ) {
        // ELEMENTO 2: TEXTOS (Entra con un poco de retraso)
        StaggeredAnimate(visible = startAnimation, index = 1) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "¡Hola de nuevo!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inicia sesión para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ELEMENTO 3: INPUT EMAIL (Más retraso)
        StaggeredAnimate(visible = startAnimation, index = 2) {
            CustomLoginTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ELEMENTO 4: INPUT PASSWORD (Más retraso aún)
        StaggeredAnimate(visible = startAnimation, index = 3) {
            CustomLoginTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                icon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        val loginButtonBrush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF1F9EBB), Color(0xFF33A1C9))
        )

        // ELEMENTO 5: BOTONES (Entran al final)
        StaggeredAnimate(visible = startAnimation, index = 4) {
            Column {
                Button(
                    onClick = { if (!uiState.isLoading) viewModel.onLoginClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(loginButtonBrush, RoundedCornerShape(50)),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Ingresar",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                uiState.genericError?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF6B6B),
                        modifier = Modifier.background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.2f))
                    Text(
                        text = "o",
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.2f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = onRegisterClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFF0E8FF)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFF0E8FF)
                    )
                ) {
                    Text(
                        text = "Crear Cuenta nueva",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showSuccessDialog) {
            SuccessDialog(onDismiss = { showSuccessDialog = false })
        }
    }
}

// --- HELPER DE ANIMACIÓN FLUIDA ---
@Composable
fun StaggeredAnimate(
    visible: Boolean,
    index: Int,
    content: @Composable () -> Unit
) {
    // Calculamos un retraso basado en el índice (0, 1, 2...)
    // Esto crea el efecto "cascada"
    val delay = index * 100

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            // Usamos Spring (Física) para suavidad extrema. LowStiffness = Movimiento relajado.
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            ),
            initialOffsetY = { 100 } // Empieza 100px abajo
        ) + fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
    ) {
        // Un LaunchedEffect dentro para aplicar el delay real visualmente si fuera necesario,
        // pero la configuración de Spring suele manejarlo bien.
        // Si queremos forzar el delay exacto, usamos tween con delayMillis,
        // pero tween es lineal. Para fluidez, Spring es mejor.
        // Combinamos la física con un modificador de layout si queremos delay exacto,
        // pero AnimatedVisibility directo con Spring es lo más limpio y fluido hoy en día.

        // OPCIÓN FLUIDA CON SPRING (La que está puesta):
        // Se siente como si los elementos flotaran hacia su lugar.
        content()
    }
}

// --- TEXT FIELD Y DIALOGO IGUAL QUE ANTES (Mantenlos igual) ---
// (He copiado tu CustomLoginTextField y SuccessDialog para que el archivo esté completo)

@Composable
fun CustomLoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType,
    isError: Boolean,
    errorMessage: String?,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {}
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
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            )
        )
        if (errorMessage != null) {
            Text(text = errorMessage, color = errorColor, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp, top = 4.dp))
        }
    }
}

@Composable
private fun SuccessDialog(onDismiss: () -> Unit) {
    val successGradientBrush = Brush.linearGradient(colors = listOf(Color(0xFF33A1C9), Color(0xFF1F9EBB)))
    Dialog(onDismissRequest = {}) {
        Surface(shape = RoundedCornerShape(28.dp), color = Color(0xFF152C58), border = BorderStroke(1.5.dp, Color(0xFF33A1C9).copy(alpha = 0.5f)), shadowElevation = 10.dp) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(80.dp).graphicsLayer(alpha = 0.99f).drawWithCache { onDrawWithContent { drawContent(); drawRect(brush = successGradientBrush, blendMode = BlendMode.SrcAtop) } })
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "¡Bienvenido!", style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sesión iniciada correctamente", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f)), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    }
    LaunchedEffect(Unit) { delay(1500L); onDismiss() }
}