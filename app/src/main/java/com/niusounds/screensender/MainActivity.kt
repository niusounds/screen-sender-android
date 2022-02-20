package com.niusounds.screensender

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.niusounds.screensender.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel by viewModels()

        lifecycleScope.launch {
            // FAB icon
            viewModel.isServiceRunning.flowWithLifecycle(lifecycle).onEach { serviceIsRunning ->
                if (serviceIsRunning) {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_stop_screen_share)
                } else {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_screen_share)
                }
            }.launchIn(this)

            // Remote address
            viewModel.ipAddressInput.flowWithLifecycle(lifecycle).onEach {
                if (binding.ipAddressInput.text.toString() != it) {
                    binding.ipAddressInput.setText(it)
                }
            }.launchIn(this)
        }

        // Notify remote address input
        binding.ipAddressInput.addTextChangedListener {
            if (it != null) {
                viewModel.ipAddressInput(it.toString())
            }
        }

        // FAB event
        binding.floatingActionButton.setOnClickListener {
            viewModel.clickFAB()
        }
    }
}
