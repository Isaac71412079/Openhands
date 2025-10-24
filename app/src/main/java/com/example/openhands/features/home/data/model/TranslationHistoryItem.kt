package com.example.openhands.features.home.data.model

/**
 * Representa una única entrada en el historial de traducciones.
 *
 * IMPORTANTE: Es necesario tener un constructor vacío () para que Firebase pueda
 * convertir los datos de vuelta a este objeto automáticamente. Los valores por defecto
 * en los parámetros (`= ""`, `= 0L`) logran esto.
 */
data class TranslationHistoryItem(
    val id: String = "",
    val originalText: String = "",
    val timestamp: Long = 0L
)