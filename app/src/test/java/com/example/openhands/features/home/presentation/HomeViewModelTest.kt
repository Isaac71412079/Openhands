package com.example.openhands.features.home.presentation

import com.example.openhands.MainDispatcherRule
import com.example.openhands.features.home.data.model.TranslationHistoryItem
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.login.data.LoginDataStore
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val homeUseCase: HomeUseCase = mockk()
    private val loginDataStore: LoginDataStore = mockk(relaxed = true)

    @Test
    fun `init debe cargar el historial correctamente`() = runTest {
        // Arrange
        val listaSimulada = listOf(
            TranslationHistoryItem(1, "Hola", 1000L),
            TranslationHistoryItem(2, "Mundo", 2000L)
        )

        // Simulamos que el UseCase devuelve un Flow con nuestra lista
        coEvery { homeUseCase.getHistory() } returns flowOf(listaSimulada)
        // Simulamos el email del usuario
        coEvery { loginDataStore.getUserEmail() } returns flowOf("test@user.com")

        // Act: Instanciar el ViewModel
        val viewModel = HomeViewModel(homeUseCase, loginDataStore)

        // --- FIX IMPORTANTE AQUÍ ---
        // Lanzamos una corrutina en segundo plano que "escuche" el flujo.
        // Esto activa el SharingStarted.WhileSubscribed y hace que el flujo emita los datos.
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.historyState.collect()
        }

        // Assert
        // Ahora sí, el valor habrá cambiado de emptyList() a listaSimulada
        assertEquals(listaSimulada, viewModel.historyState.value)
    }
}