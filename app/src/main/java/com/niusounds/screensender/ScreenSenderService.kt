package com.niusounds.screensender

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.niusounds.screensender.sender.UdpSender

class ScreenSenderService : Service() {
  private lateinit var capture: ScreenCapture
  private lateinit var sender: ScreenSender

  override fun onBind(intent: Intent?): IBinder? = null

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (intent != null) {
      val remoteAddress = intent.getStringExtra(EXTRA_REMOTE_ADDRESS)
      sender = ScreenSender(remoteAddress!!, senderFactory = UdpSender.Factory())
      sender.start()

      capture = ScreenCapture(this, sender::send)
      capture.start()
    }

    val manager = NotificationManagerCompat.from(this)
    val notificationChannel = manager.getNotificationChannel(CHANNEL_ID)
    if (notificationChannel == null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        manager.createNotificationChannel(
          NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
          ).also { it.description = CHANNEL_DESC }
        )
      }
    }

    startForeground(ID, NotificationCompat.Builder(this, CHANNEL_ID).apply {
      color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
      setColorized(true)
      setSmallIcon(R.mipmap.ic_launcher)
      setStyle(NotificationCompat.DecoratedCustomViewStyle())
      setContentTitle(NOTIFY_TITLE)
      setContentText(NOTIFY_TEXT)
    }.build())

    return START_NOT_STICKY
  }

  override fun onCreate() {
    super.onCreate()
//    registerReceiver(
//      configChangeBroadcastReciver,
//      IntentFilter("android.intent.action.CONFIGURATION_CHANGED")
//    )
  }

  override fun onDestroy() {
    capture.stop()
    sender.stop()
//    unregisterReceiver(configChangeBroadcastReciver)
    super.onDestroy()
  }

//  private val configChangeBroadcastReciver = object : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//      mirroringUsecase.restart()
//    }
//  }

  companion object {
    const val ID = 1
    const val CHANNEL_ID = "mirrorForeground"
    const val CHANNEL_NAME = "ミラーリング"
    const val CHANNEL_DESC = "録画するよ"
    const val NOTIFY_TITLE = "ScreenMirror"
    const val NOTIFY_TEXT = "ミラーリング中だよ"
    private const val EXTRA_REMOTE_ADDRESS = "remoteAddress"

    fun start(context: Context, remoteAddress: String) {
      val intent = Intent(context, ScreenSenderService::class.java).apply {
        putExtra(EXTRA_REMOTE_ADDRESS, remoteAddress)
      }
      context.startService(intent)
    }

    fun stop(context: Context) {
      val intent = Intent(context, ScreenSenderService::class.java)
      context.stopService(intent)
    }
  }
}
