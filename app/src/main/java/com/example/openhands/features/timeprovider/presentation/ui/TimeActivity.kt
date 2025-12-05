package com.example.openhands.features.timeprovider.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.openhands.R
import com.example.openhands.databinding.ActivityTimeBinding
import com.example.openhands.features.timeprovider.data.datasource.TimeLocalDataSource
import com.example.openhands.features.timeprovider.data.datasource.TimeRemoteDataSource
import com.example.openhands.features.timeprovider.data.repository.TimeRepositoryImpl
import com.example.openhands.features.timeprovider.domain.usecase.GetRealTimeUseCase
import com.example.openhands.features.timeprovider.presentation.TimeViewModel
import kotlinx.coroutines.launch

class TimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimeBinding
    private lateinit var viewModel: TimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding
        binding = ActivityTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear repo + viewmodel
        val repo = TimeRepositoryImpl(
            TimeRemoteDataSource(),
            TimeLocalDataSource(this)
        )
        viewModel = TimeViewModel(GetRealTimeUseCase(repo))

        // Collect de la hora
        lifecycleScope.launch {
            viewModel.time.collect { hora ->
                binding.textViewHora.text = hora
            }
        }

        // Cargar hora
        viewModel.loadTime()
    }
}