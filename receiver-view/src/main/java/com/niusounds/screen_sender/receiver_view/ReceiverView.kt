package com.niusounds.screen_sender.receiver_view

import android.content.Context
import android.graphics.ImageDecoder
import androidx.appcompat.widget.AppCompatImageView
import java.nio.ByteBuffer

class ReceiverView(context: Context) : AppCompatImageView(context) {

  fun setData(buffer: ByteBuffer) {
    val source = ImageDecoder.createSource(buffer)
    val drawable = ImageDecoder.decodeDrawable(source)
    setImageDrawable(drawable)
  }
}