package com.example.openhands.features.signcamera.presentation

import android.util.Log // <--- IMPORTANTE
import com.example.openhands.MainDispatcherRule
import com.example.openhands.features.signcamera.domain.repository.ISignCameraRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic // <--- IMPORTANTE
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignCameraViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val repository: ISignCameraRepository = mockk(relaxed = true)
    private val fakeSocketFlow = MutableSharedFlow<String>(replay = 1)

    @Test
    fun `init debe conectar el socket y escuchar texto detectado`() = runTest {
        // --- ARRANGE ---

        // 1. SOLUCIÓN AL ERROR: Mockeamos la clase estática Log de Android
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0 // Por si acaso usas Log.e también

        every { repository.detectedText } returns fakeSocketFlow

        // --- ACT ---
        val viewModel = SignCameraViewModel(repository)

        verify(exactly = 1) { repository.connectSocket(any()) }

        fakeSocketFlow.emit("Letra A")

        // --- ASSERT ---
        // Ahora sí pasará porque el Log.d ya no lanza error
        assertEquals("Letra A", viewModel.detectedText)

        fakeSocketFlow.emit("Hola")
        assertEquals("Hola", viewModel.detectedText)
    }

    @Test
    fun `clearText debe limpiar el texto en el repo`() = runTest {
        // También aquí mockeamos Log por si acaso el init se ejecuta
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        every { repository.detectedText } returns fakeSocketFlow
        val viewModel = SignCameraViewModel(repository)

        viewModel.clearText()

        verify { repository.clearText() }
    }
}