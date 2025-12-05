package com.example.openhands.features.timeprovider.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class TimeRemoteDataSource {

    suspend fun getServerTime(): String = withContext(Dispatchers.IO) {
        val response = URL("http://worldtimeapi.org/api/timezone/America/La_Paz").readText()
        val json = JSONObject(response)
        json.getString("datetime")
            .substring(0, 19)
            .replace("T", " ")
    }
}
