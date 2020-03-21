package com.niusounds.screensender.sender

import com.niusounds.screensender.Sender
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketAddress

class UdpSender(private val remote: SocketAddress) :
  Sender {
  private val socket = DatagramSocket()

  override fun send(data: ByteArray) {
    val packet = DatagramPacket(data, data.size, remote)
    socket.send(packet)
  }

  override fun close() {
    socket.close()
  }

  class Factory : Sender.Factory {
    override fun create(remote: SocketAddress): Sender {
      return UdpSender(remote)
    }
  }
}