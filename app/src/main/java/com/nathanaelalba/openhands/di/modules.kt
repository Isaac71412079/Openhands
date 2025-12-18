package com.nathanaelalba.openhands.di

import androidx.room.Room
import com.nathanaelalba.openhands.database.AppDatabase
import com.nathanaelalba.openhands.features.auth.presentation.RegisterViewModel
import com.nathanaelalba.openhands.features.home.data.repository.HomeRepository
import com.nathanaelalba.openhands.features.home.domain.repository.IHomeRepository
import com.nathanaelalba.openhands.features.home.domain.usecase.HomeUseCase
import com.nathanaelalba.openhands.features.home.presentation.HomeViewModel
import com.nathanaelalba.openhands.features.login.data.LoginDataStore
import com.nathanaelalba.openhands.features.login.data.repository.LoginRepository
import com.nathanaelalba.openhands.features.login.domain.repository.ILoginRepository
import com.nathanaelalba.openhands.features.login.domain.usecase.LoginUseCase
import com.nathanaelalba.openhands.features.login.presentation.LoginViewModel
import com.nathanaelalba.openhands.features.privacy_policy.data.PrivacyPolicyDataStore
import com.nathanaelalba.openhands.features.privacy_policy.viewmodel.PrivacyPolicyViewModel
import com.nathanaelalba.openhands.features.settings.data.SettingsDataStore
import com.nathanaelalba.openhands.features.settings.presentation.SettingsViewModel
import com.nathanaelalba.openhands.features.signcamera.data.repository.SignCameraRepository
import com.nathanaelalba.openhands.features.signcamera.domain.repository.ISignCameraRepository
import com.nathanaelalba.openhands.features.signcamera.presentation.SignCameraViewModel
import com.nathanaelalba.openhands.features.textsign.data.repository.TextSignRepository
import com.nathanaelalba.openhands.features.textsign.domain.repository.ITextSignRepository
import com.nathanaelalba.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import com.nathanaelalba.openhands.features.textsign.presentation.TextSignViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nathanaelalba.openhands.features.welcome.viewmodel.RemoteViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- Firebase (NUEVO) ---
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // --- Base de Datos Room ---
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "openhands-db"
        ).build()
    }
    single { get<AppDatabase>().historyDao() }

    // --- Preferences ---
    single { PrivacyPolicyDataStore(androidContext()) }
    single { SettingsDataStore(androidContext()) }
    single { LoginDataStore(androidContext()) }

    // --- ViewModels ---
    viewModel { PrivacyPolicyViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { RemoteViewModel() } // Solo de rama-app-update-notifications
    viewModel { LoginViewModel(loginUseCase = get()) }
    viewModel { RegisterViewModel(auth = get(), db = get()) }
    viewModel { HomeViewModel(homeUseCase = get(), loginDataStore = get()) }
    viewModel { TextSignViewModel(translateTextUseCase = get(), homeUseCase = get()) }
    viewModel { SignCameraViewModel(repository = get()) }

    // --- UseCases & Repositories ---
    single<ILoginRepository> { LoginRepository(get()) }
    factory { LoginUseCase(get()) }

    single<IHomeRepository> { HomeRepository(historyDao = get()) }
    factory { HomeUseCase(repository = get()) }

    single<ITextSignRepository> { TextSignRepository() }
    factory { TranslateTextUseCase(repository = get()) }

    single<ISignCameraRepository> { SignCameraRepository() }

}