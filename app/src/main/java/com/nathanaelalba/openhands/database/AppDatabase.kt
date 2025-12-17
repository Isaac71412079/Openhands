package com.nathanaelalba.openhands.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nathanaelalba.openhands.features.home.data.dao.HistoryDao
import com.nathanaelalba.openhands.features.home.data.model.TranslationHistoryItem

// 1. Anotación @Database. Se especifica la lista de entidades (tablas) y la versión.
// Es importante incrementar la versión cada vez que se modifica el esquema de la base de datos.
@Database(entities = [TranslationHistoryItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. Función abstracta para que Room provea una instancia del DAO.
    abstract fun historyDao(): HistoryDao

}
