package com.example.openhands.features.privacy_policy.viewmodel

import com.example.openhands.MainDispatcherRule
import com.example.openhands.features.privacy_policy.data.PrivacyPolicyDataStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PrivacyPolicyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mockeamos el DataStore (para no necesitar Contexto de Android real)
    private val dataStore: PrivacyPolicyDataStore = mockk(relaxed = true)

    @Test
    fun `init debe leer el estado de aceptacion correctamente`() = runTest {
        // Arrange
        // Simulamos que el DataStore dice "False" (No aceptado aún)
        val flowSimulado = MutableStateFlow(false)
        coEvery { dataStore.hasAcceptedPrivacyPolicy } returns flowSimulado

        // Act
        val viewModel = PrivacyPolicyViewModel(dataStore)

        // Assert
        // El StateFlow del ViewModel debe reflejar lo que dice el DataStore
        assertEquals(false, viewModel.hasAcceptedPrivacyPolicy.value)
    }

    @Test
    fun `acceptPrivacyPolicy debe guardar el cambio en DataStore`() = runTest {
        // Arrange
        val flowSimulado = MutableStateFlow(false)
        coEvery { dataStore.hasAcceptedPrivacyPolicy } returns flowSimulado

        val viewModel = PrivacyPolicyViewModel(dataStore)

        // Act
        viewModel.acceptPrivacyPolicy()

        // Assert
        // Verificamos que se llamó a la función de guardar con "true"
        coVerify(exactly = 1) { dataStore.setPrivacyPolicyAccepted(true) }
    }
}