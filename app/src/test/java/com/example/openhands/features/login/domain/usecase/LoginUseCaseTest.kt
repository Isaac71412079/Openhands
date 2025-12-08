package com.example.openhands.features.login.domain.usecase

import com.example.openhands.features.login.domain.model.LoginResult
import com.example.openhands.features.login.domain.repository.ILoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCaseTest {

    // Mockeamos el repositorio (simulamos su comportamiento)
    private val repository: ILoginRepository = mockk()

    // Instanciamos el UseCase con el mock
    private val loginUseCase = LoginUseCase(repository)

    @Test
    fun `invoke should return EmptyFields when email is blank`() = runBlocking {
        // Act
        val result = loginUseCase("", "password123")

        // Assert
        assertTrue(result is LoginResult.Failure.EmptyFields)
        // Verificamos que NUNCA se llamó al repositorio
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun `invoke should call repository when fields are valid`() = runBlocking {
        // Arrange (Configurar comportamiento del mock)
        coEvery { repository.login(any(), any()) } returns LoginResult.Success

        // Act
        val result = loginUseCase("test@test.com", "123456")

        // Assert
        assertTrue(result is LoginResult.Success)
        // Verificamos que SÍ se llamó al repositorio
        coVerify(exactly = 1) { repository.login("test@test.com", "123456") }
    }
}