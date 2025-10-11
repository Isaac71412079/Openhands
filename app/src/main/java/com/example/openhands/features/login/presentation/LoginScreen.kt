package com.example.openhands.features.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import com.example.openhands.R
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    val email = viewModel.email
    val password = viewModel.password
    val uiState = viewModel.uiState
    var passwordVisible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF152C58)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.openhands),
                contentDescription = "Logo de Openhands",
                modifier = Modifier.size(300.dp)
            )
            Text("Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(7.dp))

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.VisibilityOff
                    else
                        Icons.Filled.Visibility

                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = Color.White
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onLoginClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp),
                enabled = uiState !is LoginUIState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F9EBB),
                    contentColor = Color.White
                )
            ) {
                Text("Ingresar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            when (val state = uiState) {
                is LoginUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
                is LoginUIState.Success -> {
                    onLoginSuccess()
                }
                is LoginUIState.Error -> {
                    Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
                }
                is LoginUIState.Idle -> {
                }
            }
        }
    }
}