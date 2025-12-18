package com.nathanaelalba.openhands.features.auth.presentation

import com.nathanaelalba.openhands.MainDispatcherRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `registerUser should set passwordError when passwords do not match`() = runTest {
        // Arrange
        // Creamos mocks relajados (vacíos) para que no intenten conectar con Android real
        val authMock: FirebaseAuth = mockk(relaxed = true)
        val dbMock: FirebaseFirestore = mockk(relaxed = true)

        // Inyectamos los mocks
        val viewModel = RegisterViewModel(authMock, dbMock)

        // Act
        viewModel.registerUser("test@gmail.com", "123456", "654321")

        // Assert
        assertNotNull(viewModel.uiState.value.passwordError)
        assertEquals("Las contraseñas no coinciden.", viewModel.uiState.value.passwordError)

        // Aseguramos que NO se llamó a loading
        assertEquals(false, viewModel.uiState.value.isLoading)
    }
}