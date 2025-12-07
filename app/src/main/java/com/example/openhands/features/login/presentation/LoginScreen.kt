package com.example.openhands.features.login.presentation

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.openhands.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
    ) {
        // 1. Unificar en una sola disposición centrada y desplazable.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 64.dp), // Padding para no pegar al borde superior/inferior
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo Openhands",
                modifier = Modifier.height(140.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginContent(viewModel, onLoginSuccess)
        }

        // 2. El botón de retroceso se mantiene fijo en la esquina.
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver atrás",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// Ya no se necesitan los layouts separados para Portrait y Landscape

@Composable
private fun LoginContent(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    var email by rememberSaveable { mutableStateOf(viewModel.email) }
    var password by rememberSaveable { mutableStateOf(viewModel.password) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val uiState = viewModel.uiState

    LaunchedEffect(email) { viewModel.onEmailChange(email) }
    LaunchedEffect(password) { viewModel.onPasswordChange(password) }

    // 3. La columna con ancho limitado se mantiene, evitando que los campos se estiren.
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(max = 380.dp)
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        val errorGradientBrush = Brush.linearGradient(
            colors = listOf(Color(0xFFFBC02D), Color(0xFFF57C00), Color(0xFFD32F2F))
        )

        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray,
            cursorColor = Color.White,
            errorCursorColor = Color.White,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.emailError != null,
            supportingText = { uiState.emailError?.let { Text(it, style = LocalTextStyle.current.copy(brush = errorGradientBrush)) } },
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(color = Color.White),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.passwordError != null,
            supportingText = { uiState.passwordError?.let { Text(it, style = LocalTextStyle.current.copy(brush = errorGradientBrush)) } },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    Icon(imageVector = image, contentDescription = description, tint = Color.White)
                }
            },
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { if (!uiState.isLoading) viewModel.onLoginClicked() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1F9EBB),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Ingresar",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(top=8.dp))
        }

        if (uiState.success) {
            val successGradientBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF93F802), Color(0xFF00C853))
            )
            Dialog(onDismissRequest = {}) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).graphicsLayer(alpha = 0.99f).drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(brush = successGradientBrush, blendMode = BlendMode.SrcAtop)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Login Exitoso!!!",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                brush = successGradientBrush,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                delay(2000L)
                onLoginSuccess()
            }
        }

        uiState.genericError?.let {
            Text(
                text = it,
                style = LocalTextStyle.current.copy(brush = errorGradientBrush),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}