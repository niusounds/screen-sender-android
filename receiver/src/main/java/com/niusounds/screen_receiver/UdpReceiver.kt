package com.niusounds.screen_receiver

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import kotlin.concurrent.thread

class UdpReceiver(port: Int, onData: (ByteBuffer) -> Unit) {
    private val channel = DatagramChannel.open()

    init {
        channel.socket().bind(InetSocketAddress(port))

        val buf = ByteBuffer.allocate(65535)

        thread {
            runCatching {
                while (true) {
                    buf.clear()
                    channel.receive(buf)
                    buf.flip();
                    onData(buf)
                }
            }
        }
    }

    fun close() {
        channel.close()
    }
}