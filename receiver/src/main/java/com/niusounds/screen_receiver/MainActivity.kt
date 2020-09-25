package com.niusounds.screen_receiver

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.drawable.observe(this) {
            imageView.setImageDrawable(it)
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
