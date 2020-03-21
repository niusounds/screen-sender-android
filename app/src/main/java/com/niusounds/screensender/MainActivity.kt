package com.niusounds.screensender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  private val viewModel: MainViewModel by lazy {
    ViewModelProvider(this)[MainViewModel::class.java]
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    viewModel.isServiceRunning.observe(this, Observer { serviceIsRunning ->
      if (serviceIsRunning) {
        floatingActionButton.setImageResource(R.drawable.ic_stop_screen_share)
      } else {
        floatingActionButton.setImageResource(R.drawable.ic_screen_share)
      }
    })

    viewModel.ipAddressInput.observe(this, Observer {
      if (ipAddressInput.text.toString() != it) {
        ipAddressInput.setText(it)
      }
    })

    ipAddressInput.addTextChangedListener {
      if (it != null) {
        viewModel.ipAddressInput(it.toString())
      }
    }

    floatingActionButton.setOnClickListener {
      viewModel.clickFAB()
    }
  }
}
