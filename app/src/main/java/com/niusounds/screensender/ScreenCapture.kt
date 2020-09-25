package com.niusounds.screensender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.util.DisplayMetrics.DENSITY_DEFAULT
import android.view.WindowManager
import kotlin.math.min

class ScreenCapture(
  private val context: Context,
  private val callback: (Bitmap) -> Unit
) : ImageReader.OnImageAvailableListener {
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null

    fun start() {
        MediaProjectionModel.getMediaProjection(context) {
            this.mediaProjection = it
            if (it != null) {
                start(it)
            }
            print(it)
        }
    }

    private fun start(mediaProjection: MediaProjection) {
        val size = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        windowManager!!.defaultDisplay.getRealSize(size)

        val scale = min(360.0 / size.x, 640.0 / size.y)
        val w = (size.x * scale).toInt()
        val h = (size.y * scale).toInt()

        imageReader = ImageReader.newInstance(w, h, PixelFormat.RGBA_8888, 2).also {
            it.setOnImageAvailableListener(this, null)
        }
        virtualDisplay = mediaProjection.createVirtualDisplay(
          "Capture Display",
          w,
          h,
          DENSITY_DEFAULT,
          DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
          imageReader!!.surface,
          null,
          null
        )
    }

    fun stop() {
        mediaProjection?.stop()
        mediaProjection = null

        virtualDisplay?.release()
        virtualDisplay = null
    }

    override fun onImageAvailable(reader: ImageReader) {
        reader.acquireLatestImage().use { image ->
            val plane = image?.planes?.get(0) ?: return@use null
            val width = plane.rowStride / plane.pixelStride
            val bitmap = Bitmap.createBitmap(
              width,
              image.height,
              Bitmap.Config.ARGB_8888
            ).apply {
                copyPixelsFromBuffer(plane.buffer)
            }
            // imageから作成したbitmapは変な幅になっているので切り抜く
            val croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, image.width, image.height)
            callback(croppedBitmap)
        }
    }
}