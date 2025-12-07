package com.example.openhands.features.textsign.presentation

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String,
    onNavigateBack: () -> Unit
) {
    // 1. Obtener el contexto para acceder a la Activity
    val context = LocalContext.current

    // 2. DisposableEffect para gestionar el ciclo de vida de la orientación
    DisposableEffect(Unit) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        // Guardar la orientación original
        val originalOrientation = activity.requestedOrientation
        // 3. Forzar la orientación a vertical
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onDispose {
            // 4. Restaurar la orientación original al salir de la pantalla
            activity.requestedOrientation = originalOrientation
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Más Idiomas con Sign.mt", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF152C58))
            )
        }
    ) { paddingValues ->
        AndroidView(
            factory = { factoryContext -> // Renombrar para evitar shadowing
                WebView(factoryContext).apply {
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    webViewClient = WebViewClient()
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}
