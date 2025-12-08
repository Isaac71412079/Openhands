package com.example.openhands.features.login.presentation

import com.example.openhands.MainDispatcherRule
import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `onLoginClicked should update genericError when NO INTERNET or API failure occurs`() = runTest {
        // 1. Arrange: Simulamos que el UseCase devuelve un fallo con mensaje (ej: error de red)
        val networkErrorMessage = "No hay conexión a internet. Verifique su red."

        viewModel = LoginViewModel(loginUseCase)
        viewModel.onEmailChange("user@email.com")
        viewModel.onPasswordChange("pass123")

        // Forzamos al mock a devolver un error desconocido con el mensaje de red
        coEvery { loginUseCase("user@email.com", "pass123") } returns LoginResult.Failure.Unknown(networkErrorMessage)

        // 2. Act: Disparamos el evento
        viewModel.onLoginClicked()

        // 3. Assert: Verificamos que el estado de la UI tenga el error
        assertEquals(networkErrorMessage, viewModel.uiState.genericError)
        assertEquals(false, viewModel.uiState.success)
        assertEquals(false, viewModel.uiState.isLoading) // Debe dejar de cargar
    }

    @Test
    fun `onLoginClicked should update success state when login is correct`() = runTest {
        // Arrange
        viewModel = LoginViewModel(loginUseCase)
        viewModel.onEmailChange("valid@email.com")
        viewModel.onPasswordChange("validPass")

        coEvery { loginUseCase(any(), any()) } returns LoginResult.Success

        // Act
        viewModel.onLoginClicked()

        // Assert
        assertTrue(viewModel.uiState.success)
        assertEquals(null, viewModel.uiState.genericError)
    }
}