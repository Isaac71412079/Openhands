package com.example.openhands.di

import androidx.room.Room
import com.example.openhands.database.AppDatabase
import com.example.openhands.features.auth.presentation.RegisterViewModel
import com.example.openhands.features.home.data.repository.HomeRepository
import com.example.openhands.features.home.domain.repository.IHomeRepository
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.home.presentation.HomeViewModel
import com.example.openhands.features.login.data.LoginDataStore
import com.example.openhands.features.login.data.repository.LoginRepository
import com.example.openhands.features.login.domain.repository.ILoginRepository
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import com.example.openhands.features.login.presentation.LoginViewModel
import com.example.openhands.features.privacy_policy.data.PrivacyPolicyDataStore
import com.example.openhands.features.privacy_policy.viewmodel.PrivacyPolicyViewModel
import com.example.openhands.features.signcamera.data.repository.SignCameraRepository
import com.example.openhands.features.signcamera.domain.repository.ISignCameraRepository
import com.example.openhands.features.signcamera.presentation.SignCameraViewModel
import com.example.openhands.features.textsign.data.repository.TextSignRepository
import com.example.openhands.features.textsign.domain.repository.ITextSignRepository
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import com.example.openhands.features.textsign.presentation.TextSignViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    // --- Privacy Policy ---
    single { PrivacyPolicyDataStore(androidContext()) }
    viewModel { PrivacyPolicyViewModel(get()) }

    // --- Login & Auth ---
    single { LoginDataStore(androidContext()) }
    single<ILoginRepository> { LoginRepository(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(loginUseCase = get()) }

    // CAMBIO: Ahora inyectamos auth y db
    viewModel { RegisterViewModel(auth = get(), db = get()) }

    // --- Home (Ahora usa Room) ---
    single<IHomeRepository> { HomeRepository(historyDao = get()) }
    factory { HomeUseCase(repository = get()) }
    viewModel { HomeViewModel(homeUseCase = get(), loginDataStore = get()) }

    // --- TextSign ---
    single<ITextSignRepository> { TextSignRepository() }
    factory { TranslateTextUseCase(repository = get()) }
    viewModel { TextSignViewModel(translateTextUseCase = get(), homeUseCase = get()) }

    // --- SignCamera ---
    single<ISignCameraRepository> { SignCameraRepository() }
    viewModel { SignCameraViewModel(repository = get()) }
}