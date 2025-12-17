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
class RegisterValidationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Usamos mocks relajados porque solo probamos la validación local (el if inicial)
    private val auth: FirebaseAuth = mockk(relaxed = true)
    private val db: FirebaseFirestore = mockk(relaxed = true)
    private lateinit var viewModel: RegisterViewModel

    @Test
    fun `registerUser debe fallar si el email no tiene formato valido`() = runTest {
        viewModel = RegisterViewModel(auth, db)

        // Act: Email sin arroba ni dominio
        viewModel.registerUser("correo_invalido", "123456", "123456")

        // Assert
        val state = viewModel.uiState.value
        assertNotNull("Debería haber error de email", state.emailError)
        assertEquals("Correo electrónico no válido.", state.emailError)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `registerUser debe fallar si la contraseña es muy corta`() = runTest {
        viewModel = RegisterViewModel(auth, db)

        // Act: Contraseña de 3 caracteres (minimo es 6)
        viewModel.registerUser("test@gmail.com", "123", "123")

        // Assert
        val state = viewModel.uiState.value
        assertNotNull("Debería haber error de password", state.passwordError)
        assertEquals("La contraseña debe tener al menos 6 caracteres.", state.passwordError)
    }
}