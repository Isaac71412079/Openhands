package com.nathanaelalba.openhands

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nathanaelalba.openhands.features.settings.data.SettingsDataStore
import com.nathanaelalba.openhands.features.settings.presentation.SettingsViewModel
import com.nathanaelalba.openhands.navigation.AppNavigation
import com.nathanaelalba.openhands.ui.theme.OpenhandsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themePreference by settingsViewModel.themePreference.collectAsState()

            // Lógica corregida: solo es oscuro si se elige explícitamente.
            val useDarkTheme = themePreference == SettingsDataStore.THEME_DARK

            OpenhandsTheme(darkTheme = useDarkTheme) {
                AppNavigation()
            }
        }
    }
}