package com.example.openhands.di

import com.example.openhands.features.login.data.LoginDataStore
import com.example.openhands.features.login.data.repository.LoginRepository
import com.example.openhands.features.login.domain.repository.ILoginRepository
import com.example.openhands.features.login.domain.usecase.LoginUseCase
import com.example.openhands.features.login.presentation.LoginViewModel

import com.example.openhands.features.home.data.repository.HomeRepository
import com.example.openhands.features.home.domain.repository.IHomeRepository
import com.example.openhands.features.home.domain.usecase.HomeUseCase
import com.example.openhands.features.home.presentation.HomeViewModel

import com.example.openhands.features.textsign.data.repository.TextSignRepository
import com.example.openhands.features.textsign.domain.repository.ITextSignRepository
import com.example.openhands.features.textsign.domain.usecase.TranslateTextUseCase
import com.example.openhands.features.textsign.presentation.TextSignViewModel

import com.example.openhands.features.signcamera.data.repository.SignCameraRepository
import com.example.openhands.features.signcamera.domain.repository.ISignCameraRepository
import com.example.openhands.features.signcamera.presentation.SignCameraViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { LoginDataStore(androidContext()) }
    single<ILoginRepository> { LoginRepository(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }

    single<IHomeRepository> { HomeRepository() }
    factory { HomeUseCase(repository = get()) }
    viewModel { HomeViewModel(homeUseCase = get()) }

    single<ITextSignRepository> { TextSignRepository() }
    factory { TranslateTextUseCase(repository = get()) }
    viewModel { TextSignViewModel(get(), get()) }

    single<ISignCameraRepository> { SignCameraRepository() }
    viewModel { SignCameraViewModel() }

}