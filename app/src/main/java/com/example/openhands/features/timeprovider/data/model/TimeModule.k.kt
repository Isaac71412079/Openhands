package com.example.openhands.di

import com.example.openhands.features.timeprovider.presentation.TimeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val timeModule = module {
    single { FirebaseFirestore.getInstance() }  // Firestore
    viewModel { TimeViewModel(get()) }          // ViewModel
}
