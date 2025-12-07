package com.example.openhands.features.home.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Añadimos la anotación @Entity para que Room reconozca esta clase como una tabla.
@Entity(tableName = "translation_history")
data class TranslationHistoryItem(
    // 2. Definimos una PrimaryKey autoincremental, más idiomático para Room.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originalText: String,
    val timestamp: Long
)
