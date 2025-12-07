package com.example.openhands.features.textsign.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.openhands.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSignScreen(
    viewModel: TextSignViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToMoreLanguages: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFF152C58),
        bottomBar = {
            BottomInputBar(viewModel, keyboardController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Texto a Señas LSB",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Cambiar aspectRatio por una altura fija de 400.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp) // <-- Altura fija
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                val currentVideoQueue = viewModel.videoQueue
                if (currentVideoQueue.isEmpty()) {
                    VideoPlaceholder()
                } else {
                    VideoPlayer(
                        modifier = Modifier.fillMaxSize(),
                        videoQueue = currentVideoQueue
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToMoreLanguages,
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF0E8FF),
                    contentColor = Color(0xFF152C58)
                )
            ) {
                Text(
                    text = "+ Más idiomas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BottomInputBar(viewModel: TextSignViewModel, keyboardController: Any?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF152C58))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = viewModel.inputText,
            onValueChange = viewModel::onTextChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ingrese Texto...") },
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFF152C58),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                viewModel.onTranslateClicked()
                (keyboardController as? androidx.compose.ui.platform.SoftwareKeyboardController)?.hide()
            },
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xFFF0E8FF), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Traducir",
                tint = Color(0xFF152C58)
            )
        }
    }
}

@Composable
private fun VideoPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.PlayCircleOutline,
            contentDescription = "Placeholder de video",
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "El video de la seña aparecerá aquí",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
