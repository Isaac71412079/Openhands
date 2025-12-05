package com.example.openhands.features.timeprovider.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

class TimeViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _time = MutableStateFlow("Cargando...")
    val time: StateFlow<String> = _time

    fun loadTime() {
        viewModelScope.launch {
            try {
                // Suponiendo que tienes un documento con la hora actual en Firestore
                val docRef = firestore.collection("serverTime").document("now")
                docRef.get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val serverTime = document.getLong("timestamp")
                        serverTime?.let {
                            val date = Date(it)
                            val sdf = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
                            _time.value = sdf.format(date)
                        }
                    } else {
                        _time.value = "No se encontró la hora en servidor"
                    }
                }.addOnFailureListener {
                    _time.value = "Error cargando hora: ${it.message}"
                }
            } catch (e: Exception) {
                _time.value = "Excepción: ${e.message}"
            }
        }
    }
}
