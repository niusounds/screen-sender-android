package com.niusounds.screensender

import java.net.SocketAddress

interface Sender {
  fun send(data: ByteArray)
  fun close()

  interface Factory {
    fun create(remote: SocketAddress): Sender
  }
}