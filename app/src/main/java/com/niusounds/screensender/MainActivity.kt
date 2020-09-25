package com.niusounds.screensender

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: MainViewModel by viewModels()

        // FAB icon
        viewModel.isServiceRunning.observe(this) { serviceIsRunning ->
            if (serviceIsRunning) {
                floatingActionButton.setImageResource(R.drawable.ic_stop_screen_share)
            } else {
                floatingActionButton.setImageResource(R.drawable.ic_screen_share)
            }
        }

        // Remote address
        viewModel.ipAddressInput.observe(this) {
            if (ipAddressInput.text.toString() != it) {
                ipAddressInput.setText(it)
            }
        }

        // Notify remote address input
        ipAddressInput.addTextChangedListener {
            if (it != null) {
                viewModel.ipAddressInput(it.toString())
            }
        }

        // FAB event
        floatingActionButton.setOnClickListener {
            viewModel.clickFAB()
        }
    }
}
