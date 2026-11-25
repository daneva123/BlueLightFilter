package com.example.bluelightfilter

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Color
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class FilterService : Service() {
    private var overlay: View? = null
    private lateinit var wm: WindowManager
    override fun onCreate() { super.onCreate(); wm = getSystemService(WINDOW_SERVICE) as WindowManager }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val color = intent?.getIntExtra("color", Color.rgb(255,155,65)) ?: Color.rgb(255,155,65)
        val strength = intent?.getIntExtra("strength", 45) ?: 45
        if (overlay == null) {
            overlay = View(this)
            val type = if (android.os.Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            val params = WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT)
            params.gravity = Gravity.TOP or Gravity.START
            wm.addView(overlay, params)
        }
        overlay?.setBackgroundColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)))
        overlay?.alpha = (strength.coerceIn(0,100) / 100f) * 0.55f
        return START_STICKY
    }
    override fun onDestroy() { overlay?.let { wm.removeView(it) }; overlay=null; super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
}
