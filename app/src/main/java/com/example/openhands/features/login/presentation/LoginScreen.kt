package com.example.openhands.features.login.presentation

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
    onNavigateBack: () -> Unit,
    onRegisterClicked: () -> Unit // 1. Nuevo parámetro para navegar al registro
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo Openhands",
                modifier = Modifier.height(140.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginContent(viewModel, onLoginSuccess, onRegisterClicked) // 2. Pasar la acción
        }

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

@Composable
private fun LoginContent(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit // 3. Recibir la acción
) {
    var email by rememberSaveable { mutableStateOf(viewModel.email) }
    var password by rememberSaveable { mutableStateOf(viewModel.password) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val uiState = viewModel.uiState

    LaunchedEffect(email) { viewModel.onEmailChange(email) }
    LaunchedEffect(password) { viewModel.onPasswordChange(password) }

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

        Spacer(modifier = Modifier.height(8.dp))

        uiState.genericError?.let {
            Text(
                text = it,
                style = LocalTextStyle.current.copy(brush = errorGradientBrush),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 4. Divisor "o"
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.5f))
            Text(
                text = "o",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.5f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 5. Botón "Crear Cuenta nueva"
        Button(
            onClick = onRegisterClicked,
            modifier = Modifier.fillMaxWidth().height(49.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF0E8FF),
                contentColor = Color(0xFF152C58)
            )
        ) {
            Text(
                text = "Crear Cuenta nueva",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(top=16.dp))
        }

        if (uiState.success) {
            val successGradientBrush = Brush.linearGradient(
                colors = listOf(Color(0xFF6CB600), Color(0xFF00CE1E))
            )
            Dialog(onDismissRequest = {}) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black,
                    border = BorderStroke(2.dp, Color.Green)
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
    }
}