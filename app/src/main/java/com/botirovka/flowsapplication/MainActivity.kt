package com.botirovka.flowsapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.botirovka.flowsapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btnLiveData.setOnClickListener      {
            viewModel.triggerLiveData()
        }

        binding.btnStateFlow.setOnClickListener {
            viewModel.triggerStateFlow()
        }

        binding.btnSharedFlow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }

        binding.btnChannel.setOnClickListener {
            viewModel.triggerChannel()
        }

        subscribeToObservables()


        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun subscribeToObservables() {
        Log.d("mydebug", "LiveData subscribe")
        viewModel.liveData.observe(this) {
            binding.tvLiveData.text = it
        }

        //Підписка на StateFlow
        lifecycleScope.launch {
            Log.d("mydebug", "StateFlow subscribe")
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.stateFlow.collectLatest {
                    binding.tvStateFlow.text = it
                }
            }
        }
        //Підписка на SharedFlow
        lifecycleScope.launch {
            Log.d("mydebug", "StateFlow subscribe")
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.sharedFlow.collectLatest {
                    binding.tvSharedFlow.text = it
                }
            }
        }

        //Підписка на Flow
        lifecycleScope.launch {
            delay(5000)
            Log.d("mydebug", "Flow subscribe")
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.flow.collectLatest {
                    binding.tvFlow.text = it
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val message = viewModel._channel.receive()
                binding.tvChannel.text = message
            }
        }
    }

}