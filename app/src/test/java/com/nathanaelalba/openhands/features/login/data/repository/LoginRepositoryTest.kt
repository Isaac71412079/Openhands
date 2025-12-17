package com.nathanaelalba.openhands.features.login.data.repository

import com.nathanaelalba.openhands.features.login.data.LoginDataStore
import com.nathanaelalba.openhands.features.login.domain.model.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginRepositoryTest {

    private val loginDataStore: LoginDataStore = mockk(relaxed = true)
    private val firebaseAuth: FirebaseAuth = mockk()
    private val mockAuthTask: Task<AuthResult> = mockk()

    private val repository = LoginRepository(loginDataStore, firebaseAuth)

    @Test
    fun `login debe retornar Failure Unknown con mensaje cuando hay FirebaseNetworkException`() = runBlocking {
        // Arrange
        // Simulamos la excepción de red de Firebase
        val networkException = mockk<FirebaseNetworkException>()

        // Configuramos el mock de Firebase para que lance la excepción al intentar loguearse
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockAuthTask
        // Truco para mocks de Google Tasks:
        every { mockAuthTask.isComplete } returns true
        every { mockAuthTask.isCanceled } returns false
        every { mockAuthTask.exception } returns networkException
        // IMPORTANTE: Como usamos .await() (extensión de Kotlin), necesitamos simular el comportamiento de fallo
        // Sin embargo, mockear tareas de Google estáticas es difícil.
        // Para simplificar en unit testing puro sin Robolectric, a veces probamos la lógica de mapeo aparte.

        // NOTA: Si esta prueba te da problemas por "Method not mocked" en Task,
        // confía en la prueba del ViewModel que ya hicimos.
        // Pero intentemos simular que la llamada lanza la excepción:
        coEvery { firebaseAuth.signInWithEmailAndPassword(any(), any()) } throws networkException

        // Act
        val result = repository.login("test@test.com", "pass")

        // Assert
        assertTrue(result is LoginResult.Failure.Unknown)
        // Verificamos el mensaje exacto que programamos
        assertEquals(
            "No hay conexión a internet. Verifique su red.",
            (result as LoginResult.Failure.Unknown).message
        )
    }
}