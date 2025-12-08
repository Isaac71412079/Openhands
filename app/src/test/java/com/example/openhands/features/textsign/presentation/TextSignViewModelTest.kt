package com.example.openhands.features.textsign.presentation

import com.example.openhands.MainDispatcherRule
import com.example.openhands.R
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TextSignViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val translateUseCase: TranslateTextUseCase = mockk(relaxed = true)
    private val homeUseCase: HomeUseCase = mockk(relaxed = true) // Mockeamos para que no intente guardar en DB real
    private lateinit var viewModel: TextSignViewModel

    @Test
    fun `onTranslateClicked debe detectar palabras completas (excepciones)`() = runTest {
        viewModel = TextSignViewModel(translateUseCase, homeUseCase)

        // Act: Escribimos "hola" (que está en tu mapa de excepciones)
        viewModel.onTextChanged("hola")
        viewModel.onTranslateClicked()

        // Assert
        val queue = viewModel.videoQueue
        assertEquals("Debería haber 1 solo video", 1, queue.size)
        // Verificamos que el video sea el de "hola" y no deletreado h-o-l-a
        assertEquals(R.raw.hola, queue[0])
    }

    @Test
    fun `onTranslateClicked debe deletrear palabras desconocidas`() = runTest {
        viewModel = TextSignViewModel(translateUseCase, homeUseCase)

        // Act: Escribimos "ala" (a-l-a)
        viewModel.onTextChanged("ala")
        viewModel.onTranslateClicked()

        // Assert
        val queue = viewModel.videoQueue
        assertEquals("Debería tener 3 videos (3 letras)", 3, queue.size)
        assertEquals(R.raw.letra_a, queue[0])
        assertEquals(R.raw.letra_l, queue[1])
        assertEquals(R.raw.letra_a, queue[2])
    }

    @Test
    fun `onTranslateClicked debe guardar en el historial`() = runTest {
        viewModel = TextSignViewModel(translateUseCase, homeUseCase)

        viewModel.onTextChanged("prueba")
        viewModel.onTranslateClicked()

        // Verificamos que se llamó al caso de uso de Home para guardar
        coVerify(exactly = 1) { homeUseCase.saveTranslation("prueba") }
    }
}