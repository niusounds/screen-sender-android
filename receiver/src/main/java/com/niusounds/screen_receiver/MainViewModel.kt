package com.niusounds.screen_receiver

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException

class MainViewModel : ViewModel() {

    val drawable: LiveData<Drawable> get() = _drawable
    private val _drawable = MutableLiveData<Drawable>()

    private var receiver: UdpReceiver? = null

    fun startReceiving() {
        if (receiver != null) return

        receiver = UdpReceiver(port = 9999, onData = { buffer ->
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(buffer)
                    val drawable = ImageDecoder.decodeDrawable(source)
                    _drawable.postValue(drawable)
                } else {
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    _drawable.postValue(BitmapDrawable(Resources.getSystem(), bitmap))
                }
            } catch (e: IOException) {
                Log.d("ReceiverView", "decode failed $e")
            }
        })
    }

    fun stopReceiving() {
        receiver?.close()
        receiver = null
    }
}