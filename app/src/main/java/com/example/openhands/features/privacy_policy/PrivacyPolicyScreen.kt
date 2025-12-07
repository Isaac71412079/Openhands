package com.example.openhands.features.privacy_policy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.openhands.R

@Composable
fun PrivacyPolicyScreen(
    onContinueClicked: () -> Unit,
) {
    var isChecked by remember { mutableStateOf(false) }

    val blueTextColor = Color(0xFF0B1733)

    // Degradado de fondo
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFCEC8FF),
            Color(0xFFB0A3FF),
            Color(0xFF9F92FF)
        )
    )

    Scaffold(
        containerColor = Color(0xFF152C58),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it }
                        )
                        Spacer(modifier = Modifier.width(2.dp))

                        // *** Texto completo con salto de línea ***
                        Text(
                            text = stringResource(R.string.accept_terms_and_conditions),
                            modifier = Modifier.weight(1f),
                            softWrap = true,
                            color = blueTextColor
                        )
                    }

                    Button(
                        onClick = onContinueClicked,
                        enabled = isChecked
                    ) {
                        Text(stringResource(R.string.continue_button))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp, vertical = 7.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(backgroundBrush)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                stringResource(R.string.privacy_policy_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = blueTextColor
            )

            Spacer(modifier = Modifier.height(4.dp))
            Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp), color = blueTextColor)
            Text(
                stringResource(R.string.privacy_policy_last_updated),
                style = MaterialTheme.typography.bodySmall,
                color = blueTextColor
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp), color = blueTextColor)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.privacy_policy_intro),
                color = blueTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                stringResource(R.string.privacy_policy_info_collection_intro),
                color = blueTextColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Secciones
            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_camera_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_camera_description),
                    stringResource(R.string.privacy_policy_camera_collection),
                    stringResource(R.string.privacy_policy_camera_processing),
                    stringResource(R.string.privacy_policy_camera_storage)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_text_to_sign_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_text_to_sign_description),
                    stringResource(R.string.privacy_policy_text_to_sign_source),
                    stringResource(R.string.privacy_policy_text_to_sign_data_sharing),
                    stringResource(R.string.privacy_policy_text_to_sign_third_party)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_history_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_history_description),
                    stringResource(R.string.privacy_policy_history_privacy),
                    stringResource(R.string.privacy_policy_history_access)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_permissions_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_permissions_description),
                    stringResource(R.string.privacy_policy_permission_camera),
                    stringResource(R.string.privacy_policy_permission_internet)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_security_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_security_description)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_disclaimer_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_disclaimer_description)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_children_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_children_description)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_changes_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_changes_description)
                ),
                textColor = blueTextColor
            )

            PrivacyPolicySection(
                title = stringResource(R.string.privacy_policy_contact_title),
                content = listOf(
                    stringResource(R.string.privacy_policy_contact_description),
                    stringResource(R.string.privacy_policy_contact_email)
                ),
                textColor = blueTextColor
            )
        }
    }
}

@Composable
fun PrivacyPolicySection(title: String, content: List<String>, textColor: Color) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = textColor
    )
    Spacer(modifier = Modifier.height(8.dp))

    content.forEach {
        Text(it, color = textColor)
        Spacer(modifier = Modifier.height(4.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))
}
