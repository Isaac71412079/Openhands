package com.nathanaelalba.openhands.features.privacy_policy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.nathanaelalba.openhands.features.settings.data.SettingsDataStore
import com.nathanaelalba.openhands.features.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    showAppBar: Boolean = false,
    onContinueClicked: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    var isChecked by remember { mutableStateOf(false) }
    val themePreference by settingsViewModel.themePreference.collectAsState()

    // --- Lógica de Tema --- 
    val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK
    val scaffoldBackgroundColor = if (useDarkTheme) Color.Black else Color(0xFF152C58)
    val surfaceColor = if (useDarkTheme) Color(0xFF1C1C1E) else Color(0xFFF9F9FF)
    val textColorOnSurface = if (useDarkTheme) Color.White.copy(alpha = 0.87f) else Color(0xFF1A1A1A)
    
    val brandGradient = Brush.horizontalGradient(colors = listOf(Color(0xFF33A1C9), Color(0xFFD946EF)))

    Scaffold(
        containerColor = scaffoldBackgroundColor,
        topBar = {
            if (showAppBar) {
                TopAppBar(
                    title = { Text("Política de Privacidad", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = scaffoldBackgroundColor)
                )
            }
        },
        bottomBar = {
            if (!showAppBar) {
                PrivacyBottomBar(
                    isChecked = isChecked,
                    onCheckedChange = { isChecked = it },
                    onContinueClicked = onContinueClicked,
                    gradientBrush = brandGradient,
                    useDarkTheme = useDarkTheme
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showAppBar) {
                Spacer(modifier = Modifier.height(16.dp))
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
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = surfaceColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
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

                    Divider(modifier = Modifier.padding(vertical = 16.dp), color = (if (useDarkTheme) Color.DarkGray else Color.LightGray).copy(alpha = 0.5f))

                    Text(stringResource(R.string.privacy_policy_intro), color = textColorOnSurface, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.privacy_policy_info_collection_intro), color = textColorOnSurface, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(24.dp))

                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_camera_title), content = listOf(stringResource(R.string.privacy_policy_camera_description), stringResource(R.string.privacy_policy_camera_collection), stringResource(R.string.privacy_policy_camera_processing), stringResource(R.string.privacy_policy_camera_storage)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_text_to_sign_title), content = listOf(stringResource(R.string.privacy_policy_text_to_sign_description), stringResource(R.string.privacy_policy_text_to_sign_source), stringResource(R.string.privacy_policy_text_to_sign_data_sharing), stringResource(R.string.privacy_policy_text_to_sign_third_party)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_history_title), content = listOf(stringResource(R.string.privacy_policy_history_description), stringResource(R.string.privacy_policy_history_privacy), stringResource(R.string.privacy_policy_history_access)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_permissions_title), content = listOf(stringResource(R.string.privacy_policy_permissions_description), stringResource(R.string.privacy_policy_permission_camera), stringResource(R.string.privacy_policy_permission_internet)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_security_title), content = listOf(stringResource(R.string.privacy_policy_security_description)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_disclaimer_title), content = listOf(stringResource(R.string.privacy_policy_disclaimer_description)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_children_title), content = listOf(stringResource(R.string.privacy_policy_children_description)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_changes_title), content = listOf(stringResource(R.string.privacy_policy_changes_description)), textColor = textColorOnSurface, titleGradient = brandGradient)
                    PrivacyPolicySection(title = stringResource(R.string.privacy_policy_contact_title), content = listOf(stringResource(R.string.privacy_policy_contact_description), stringResource(R.string.privacy_policy_contact_email)), textColor = textColorOnSurface, titleGradient = brandGradient)

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
    gradientBrush: Brush,
    useDarkTheme: Boolean
) {
    val bottomBarColor = if (useDarkTheme) Color.Black else Color(0xFF152C58)
    Surface(color = bottomBarColor, tonalElevation = 8.dp, shadowElevation = 16.dp) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!isChecked) }) {
                Checkbox(checked = isChecked, onCheckedChange = null, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF33A1C9), uncheckedColor = Color.White.copy(alpha = 0.6f), checkmarkColor = Color.White))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.accept_terms_and_conditions), style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f), modifier = Modifier.padding(top = 12.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onContinueClicked, enabled = isChecked, modifier = Modifier.fillMaxWidth().height(56.dp).background(brush = if (isChecked) gradientBrush else Brush.linearGradient(listOf(Color.Gray, Color.Gray)), shape = RoundedCornerShape(50)), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent, disabledContentColor = Color.White.copy(alpha = 0.5f)), contentPadding = PaddingValues()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (!isChecked) {
                        Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.1f)))
                    }
                    Text(text = stringResource(R.string.continue_button).uppercase(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicySection(title: String, content: List<String>, textColor: Color, titleGradient: Brush) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, style = MaterialTheme.typography.titleMedium.copy(brush = titleGradient), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        content.forEach { paragraph ->
            Text(text = paragraph, color = textColor, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp, modifier = Modifier.padding(bottom = 6.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}