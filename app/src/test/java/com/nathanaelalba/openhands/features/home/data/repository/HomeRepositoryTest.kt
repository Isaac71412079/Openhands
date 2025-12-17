package com.nathanaelalba.openhands.features.home.data.repository

import com.nathanaelalba.openhands.features.home.data.dao.HistoryDao
import com.nathanaelalba.openhands.features.home.data.model.TranslationHistoryItem
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeRepositoryTest {

    private val historyDao: HistoryDao = mockk(relaxed = true) // relaxed=true permite no definir cada respuesta void
    private val repository = HomeRepository(historyDao)

    @Test
    fun `saveTranslation should insert item via DAO`() = runBlocking {
        // Arrange
        val textToSave = "Hola Mundo"
        val slot = slot<TranslationHistoryItem>() // Capturador de argumentos

        // Act
        repository.saveTranslation(textToSave)

        // Assert
        // Verificamos que se llamó al insert con un objeto que contiene el texto correcto
        coVerify { historyDao.insert(capture(slot)) }

        assertEquals("Hola Mundo", slot.captured.originalText)
    }

    @Test
    fun `clearHistory should call clearAll on DAO`() = runBlocking {
        // Act
        repository.clearHistory()

        // Assert
        coVerify { historyDao.clearAll() }
    }
}