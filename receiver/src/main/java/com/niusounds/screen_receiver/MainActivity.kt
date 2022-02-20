package com.niusounds.screen_receiver

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.niusounds.screen_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.drawable.observe(this) {
            binding.imageView.setImageDrawable(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startReceiving()
    }

    override fun onPause() {
        viewModel.stopReceiving()
        super.onPause()
    }
}
