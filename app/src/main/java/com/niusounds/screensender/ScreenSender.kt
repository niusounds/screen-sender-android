package com.niusounds.screensender

import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ScreenSender(
  private val remoteAddress: String,
  private val port: Int = 9999,
  private val senderFactory: Sender.Factory
) {
  private val queue = ArrayBlockingQueue<Bitmap>(2)
  private val executor = Executors.newSingleThreadExecutor()
  private var future: Future<*>? = null

  fun send(bitmap: Bitmap) {
    queue.offer(bitmap)
  }

  fun start() {
    future = executor.submit {
      val sender = senderFactory.create(InetSocketAddress(remoteAddress, port))

      runCatching {
        while (!Thread.interrupted()) {
          val bitmap = queue.take()
          val data = bitmap.toByteArray(Bitmap.CompressFormat.WEBP, 50)
          bitmap.recycle()

          sender.send(data)
        }
      }.exceptionOrNull()?.printStackTrace()

      Log.d("ScreenSender", "send loop exited")
    }
  }

  fun stop() {
    future?.cancel(true)
    future = null
    executor.shutdown()
  }
}

private fun Bitmap.toByteArray(format: Bitmap.CompressFormat, quality: Int): ByteArray {
  val os = ByteArrayOutputStream()
  compress(format, quality, os)
  return os.toByteArray()
}