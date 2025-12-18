package com.nathanaelalba.openhands.features.home.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathanaelalba.openhands.features.home.data.model.TranslationHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TranslationHistoryItem)

    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC")
    fun getHistory(): Flow<List<TranslationHistoryItem>>

    // 1. Nueva función para borrar todos los registros de la tabla.
    @Query("DELETE FROM translation_history")
    suspend fun clearAll()
}
