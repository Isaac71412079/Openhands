package com.example.openhands.features.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF152C58)
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Botón de registro
            Button(
                onClick = {
                    viewModel.registerUser(
                        email,
                        password,
                        confirmPassword
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Registrarse")
            }

            if (uiState.isLoading) CircularProgressIndicator()

            if (uiState.success) {
                LaunchedEffect(Unit) {
                    onRegisterSuccess()
                }
            }

            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}