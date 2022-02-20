package com.niusounds.screensender

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.niusounds.screensender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel by viewModels()

        // FAB icon
        viewModel.isServiceRunning.observe(this) { serviceIsRunning ->
            if (serviceIsRunning) {
                binding.floatingActionButton.setImageResource(R.drawable.ic_stop_screen_share)
            } else {
                binding.floatingActionButton.setImageResource(R.drawable.ic_screen_share)
            }
        }

        // Remote address
        viewModel.ipAddressInput.observe(this) {
            if (binding.ipAddressInput.text.toString() != it) {
                binding.ipAddressInput.setText(it)
            }
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
