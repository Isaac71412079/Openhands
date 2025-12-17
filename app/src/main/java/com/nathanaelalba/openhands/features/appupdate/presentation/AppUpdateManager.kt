package com.nathanaelalba.openhands.features.appupdate.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nathanaelalba.openhands.features.appupdate.data.AppUpdateRepository
import com.nathanaelalba.openhands.features.appupdate.domain.AppUpdateConfig
import com.nathanaelalba.openhands.features.appupdate.domain.AppUpdateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppUpdateManager {

    private val repository = AppUpdateRepository()
    private val useCase = AppUpdateUseCase(repository)

    suspend fun shouldShowUpdate(currentVersion: Int): AppUpdateConfig? {
        return useCase.shouldShowUpdate(currentVersion)
    }

    @Composable
    fun UpdateDialog(config: AppUpdateConfig, onDismiss: () -> Unit) {
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(config.title) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(config.message)
                    Spacer(modifier = Modifier.height(8.dp))
                    config.changelog.forEach { item ->
                        Text("• $item")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(config.playStoreUrl))
                    context.startActivity(intent)
                    CoroutineScope(Dispatchers.IO).launch {
                        useCase.markAsSeen(config.latestVersionCode)
                    }
                    onDismiss()
                }) {
                    Text("Actualizar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        useCase.markAsSeen(config.latestVersionCode)
                    }
                    onDismiss()
                }) {
                    Text("Más tarde")
                }
            }
        )
    }
}
