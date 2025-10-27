package com.example.openhands.features.textsign.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.openhands.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSignScreen(
    viewModel: TextSignViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.openhands),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF152C58)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "TRADUCCIÓN DE TEXTO A SEÑAS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )


            // --- INICIO DE LA MODIFICACIÓN ---

            // 1. Espacio para mostrar la imagen.
            // Usamos un Box para que ocupe espacio aunque no haya imagen.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // Damos una altura fija al contenedor de la imagen
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Si hay una imagen para mostrar en el ViewModel, la pintamos.
                viewModel.imageResId?.let { imageId ->
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = "Imagen de la seña",
                        modifier = Modifier.size(200.dp) // Tamaño de la imagen
                    )
                }
            }

            // 2. El campo de texto se ajusta
            OutlinedTextField(
                value = viewModel.inputText,
                onValueChange = viewModel::onTextChanged,
                modifier = Modifier
                    .fillMaxWidth(), // Quitamos la altura fija de 200.dp
                placeholder = { Text("Ingrese Texto...") },
                shape = RoundedCornerShape(20.dp),
                singleLine = true, // Ideal para una sola palabra o letra
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    cursorColor = Color(0xFF152C58),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. El botón se mantiene igual, su lógica está en el ViewModel
            Button(
                onClick = viewModel::onTranslateClicked,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(57.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF0E8FF),
                    contentColor = Color(0xFF152C58)
                )
            ) {
                Text(
                    text = "Traducir",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}