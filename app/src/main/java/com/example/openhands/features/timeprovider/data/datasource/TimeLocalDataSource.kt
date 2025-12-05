package com.example.openhands.features.timeprovider.data.datasource

import android.content.Context

class TimeLocalDataSource(context: Context) {

    private val prefs = context.getSharedPreferences("time_cache", Context.MODE_PRIVATE)

    fun saveTime(time: String) {
        prefs.edit().putString("last_time", time).apply()
    }

    fun getLastTime(): String? {
        return prefs.getString("last_time", null)
    }
}
