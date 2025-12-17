package com.nathanaelalba.openhands.features.privacy_policy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nathanaelalba.openhands.R

@Composable
fun PrivacyPolicyScreen(
    onContinueClicked: () -> Unit,
) {
    var isChecked by remember { mutableStateOf(false) }

    // --- COLORES DE MARCA ---
    val darkBlueBackground = Color(0xFF152C58)
    val textDark = Color(0xFF1A1A1A)

    // EL GRADIENTE "Celeste-Rosa" (Cyan a Magenta/Rosa)
    val brandGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF33A1C9), // Celeste OpenHands
            Color(0xFFD946EF)  // Rosa/Magenta Vibrante
        )
    )

    Scaffold(
        containerColor = darkBlueBackground,
        bottomBar = {
            // Barra inferior personalizada (sin usar el BottomAppBar por defecto para más control)
            PrivacyBottomBar(
                isChecked = isChecked,
                onCheckedChange = { isChecked = it },
                onContinueClicked = onContinueClicked,
                gradientBrush = brandGradient
            )
        }
    ) { paddingValues ->

        // Contenedor principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Título Principal con estilo
            Text(
                text = stringResource(R.string.privacy_policy_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Por favor, lee atentamente antes de continuar.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TARJETA DE CONTENIDO (Papel Blanco) ---
            // Usamos Surface para elevación y esquinas redondeadas
            Surface(
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = Color(0xFFF9F9FF), // Blanco hueso muy suave para lectura cómoda
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    // Fecha de actualización estilizada
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp, 24.dp)
                                .background(brandGradient, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.privacy_policy_last_updated),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    Text(
                        stringResource(R.string.privacy_policy_intro),
                        color = textDark,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        stringResource(R.string.privacy_policy_info_collection_intro),
                        color = textDark,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Secciones
                    // Pasamos el brush del gradiente para colorear los títulos
                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_camera_title),
                        content = listOf(
                            stringResource(R.string.privacy_policy_camera_description),
                            stringResource(R.string.privacy_policy_camera_collection),
                            stringResource(R.string.privacy_policy_camera_processing),
                            stringResource(R.string.privacy_policy_camera_storage)
                        ),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_text_to_sign_title),
                        content = listOf(
                            stringResource(R.string.privacy_policy_text_to_sign_description),
                            stringResource(R.string.privacy_policy_text_to_sign_source),
                            stringResource(R.string.privacy_policy_text_to_sign_data_sharing),
                            stringResource(R.string.privacy_policy_text_to_sign_third_party)
                        ),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_history_title),
                        content = listOf(
                            stringResource(R.string.privacy_policy_history_description),
                            stringResource(R.string.privacy_policy_history_privacy),
                            stringResource(R.string.privacy_policy_history_access)
                        ),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_permissions_title),
                        content = listOf(
                            stringResource(R.string.privacy_policy_permissions_description),
                            stringResource(R.string.privacy_policy_permission_camera),
                            stringResource(R.string.privacy_policy_permission_internet)
                        ),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_security_title),
                        content = listOf(stringResource(R.string.privacy_policy_security_description)),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_disclaimer_title),
                        content = listOf(stringResource(R.string.privacy_policy_disclaimer_description)),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_children_title),
                        content = listOf(stringResource(R.string.privacy_policy_children_description)),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_changes_title),
                        content = listOf(stringResource(R.string.privacy_policy_changes_description)),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    PrivacyPolicySection(
                        title = stringResource(R.string.privacy_policy_contact_title),
                        content = listOf(
                            stringResource(R.string.privacy_policy_contact_description),
                            stringResource(R.string.privacy_policy_contact_email)
                        ),
                        textColor = textDark,
                        titleGradient = brandGradient
                    )

                    // Espacio extra al final para que no choque con el borde
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun PrivacyBottomBar(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onContinueClicked: () -> Unit,
    gradientBrush: Brush
) {
    Surface(
        color = Color(0xFF152C58), // Fondo oscuro
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Checkbox Row
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCheckedChange(!isChecked) } // Click en todo el texto
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = null, // Manejado por el Row clickable
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF33A1C9), // Cyan al marcar
                        uncheckedColor = Color.White.copy(alpha = 0.6f),
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.accept_terms_and_conditions),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 12.dp) // Alinear visualmente con el checkbox
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Gradiente "Celeste-Rosa"
            Button(
                onClick = onContinueClicked,
                enabled = isChecked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = if (isChecked) gradientBrush else
                            Brush.linearGradient(listOf(Color.Gray, Color.Gray)), // Gris si deshabilitado
                        shape = RoundedCornerShape(50)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Transparente para ver el gradiente
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                contentPadding = PaddingValues() // Necesario para el background custom
            ) {
                // Caja interna para centrar contenido
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isChecked) {
                        // Fondo gris visual si está deshabilitado
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                    }

                    Text(
                        text = stringResource(R.string.continue_button).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicySection(
    title: String,
    content: List<String>,
    textColor: Color,
    titleGradient: Brush
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Título con Gradiente (Texto coloreado)
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                brush = titleGradient // ¡Aquí aplicamos el rosa-celeste al texto!
            ),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        content.forEach { paragraph ->
            Text(
                text = paragraph,
                color = textColor.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}