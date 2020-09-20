package com.niusounds.screen_sender.receiver_view

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    UdpReceiver(port = 9999, onData = {
      try {
        val source = ImageDecoder.createSource(it)
        val drawable = ImageDecoder.decodeDrawable(source)

        runOnUiThread {
          imageView.setImageDrawable(drawable)
        }

      } catch (e: IOException) {
        Log.d("ReceiverView", "decode failed $e")
      }
    })
  }
}
