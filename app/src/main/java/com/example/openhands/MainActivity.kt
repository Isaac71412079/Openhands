package com.example.openhands

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.util.Log
import com.example.openhands.navigation.AppNavigation
import com.example.openhands.ui.theme.OpenhandsTheme

// 🔥 Firebase
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenhandsTheme {
                AppNavigation()
            }
        }
    }

    private fun testFirebaseConfig() {
        try {
            val auth = Firebase.auth
            val db = Firebase.firestore
            Log.d("FirebaseTest", "Firebase Auth OK: ${auth != null}")
            Log.d("FirebaseTest", "Firebase Firestore OK: ${db != null}")
            Toast.makeText(this, "Firebase Config OK", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("FirebaseTest", "Error: ${e.message}")
            Toast.makeText(this, "Firebase ERROR: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
