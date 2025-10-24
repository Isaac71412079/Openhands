package com.example.openhands.di

// Imports de Login
import com.example.openhands.features.login.data.LoginDataStore
import com.example.openhands.features.login.data.repository.LoginRepository
import com.example.openhands.features.login.domain.repository.ILoginRepository
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import com.example.openhands.features.login.presentation.LoginViewModel

// Imports de Home
import com.example.openhands.features.home.data.repository.HomeRepository
import com.example.openhands.features.home.domain.repository.IHomeRepository
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.home.presentation.HomeViewModel

// Imports de TextSign
import com.example.openhands.features.textsign.data.repository.TextSignRepository
import com.example.openhands.features.textsign.domain.repository.ITextSignRepository
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import com.example.openhands.features.textsign.presentation.TextSignViewModel

// Imports de SignCamera (ahora deberían funcionar porque el paquete se llama 'signcamera')
import com.example.openhands.features.signcamera.data.repository.SignCameraRepository
import com.example.openhands.features.signcamera.domain.repository.ISignCameraRepository
import com.example.openhands.features.signcamera.presentation.SignCameraViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- LOGIN ---
    single { LoginDataStore(androidContext()) }
    single<ILoginRepository> { LoginRepository(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }

    // --- HOME ---
    single<IHomeRepository> { HomeRepository() }
    factory { HomeUseCase(repository = get()) }
    viewModel { HomeViewModel(homeUseCase = get()) }

    // --- TEXTSIGN ---
    single<ITextSignRepository> { TextSignRepository() }
    factory { TranslateTextUseCase(repository = get()) }
    // CORREGIDO: Le pasamos las dos dependencias que necesita
    viewModel { TextSignViewModel(get(), get()) }

    // --- SIGNCAMERA ---
    single<ISignCameraRepository> { SignCameraRepository() }
    viewModel { SignCameraViewModel() }

}