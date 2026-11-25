package com.example.bluelightfilter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private var enabled = false
    private var tint = Color.rgb(255, 155, 65)
    private lateinit var status: TextView
    private lateinit var toggle: Button
    private lateinit var preview: View
    private lateinit var strength: SeekBar
    private var strengthValue = 45

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(Color.rgb(9, 13, 24))
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(28, 34, 28, 24); gravity = Gravity.CENTER_HORIZONTAL }
        fun text(s: String, size: Float, color: Int = Color.WHITE, bold: Boolean = false) = TextView(this).apply { text=s; textSize=size; setTextColor(color); if(bold) setTypeface(null, 1); gravity=Gravity.CENTER }
        root.addView(text("BLUE LIGHT FILTER", 13f, Color.rgb(255,160,90), true))
        root.addView(text("PRO", 34f, Color.WHITE, true).apply { setPadding(0,4,0,18) })
        status = text("Your screen is currently normal", 14f, Color.LTGRAY)
        root.addView(status)
        preview = View(this).apply { setBackgroundColor(Color.rgb(255,155,65)); alpha=0.18f }
        root.addView(preview, LinearLayout.LayoutParams(180, 110).apply { topMargin=24; bottomMargin=20 })
        toggle = Button(this).apply { text="TURN FILTER ON"; setOnClickListener { toggleFilter() } }
        root.addView(toggle, LinearLayout.LayoutParams(-1, 56))
        root.addView(text("FILTER STRENGTH  ·  $strengthValue%", 14f, Color.WHITE, true).apply { tag="strengthLabel"; setPadding(0,28,0,8) })
        strength = SeekBar(this).apply { max=100; progress=strengthValue; setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, user: Boolean) { strengthValue=p; (root.findViewWithTag<TextView>("strengthLabel"))?.text="FILTER STRENGTH  ·  $p%"; preview.alpha=p/100f; updateOverlay() }
            override fun onStartTrackingTouch(s: SeekBar?) {} override fun onStopTrackingTouch(s: SeekBar?) {}
        }) }
        root.addView(strength, LinearLayout.LayoutParams(-1, 48))
        root.addView(text("CUSTOM FILTER COLOR", 14f, Color.WHITE, true).apply { setPadding(0,26,0,12) })
        val colors = listOf("Warm Orange" to Color.rgb(255,155,65), "Soft Yellow" to Color.rgb(255,220,110), "Rose" to Color.rgb(255,105,125), "Amber" to Color.rgb(255,190,75))
        colors.forEach { (name, color) ->
            val b = Button(this).apply { text=name; setTextColor(Color.WHITE); setBackgroundColor(Color.rgb(28,35,52)); setOnClickListener { tint=color; preview.setBackgroundColor(color); updateOverlay() } }
            root.addView(b, LinearLayout.LayoutParams(-1, 46).apply { topMargin=5 })
        }
        root.addView(text("Manual mode · No automatic schedule", 12f, Color.LTGRAY).apply { setPadding(0,24,0,0) })
        root.addView(text("Overlay permission is required to tint other apps.", 11f, Color.GRAY).apply { setPadding(0,12,0,0) })
        setContentView(ScrollView(this).apply { addView(root) })
    }

    private fun toggleFilter() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
            Toast.makeText(this, "Allow Display over other apps, then tap ON again.", Toast.LENGTH_LONG).show(); return
        }
        enabled = !enabled
        if (enabled) { startService(Intent(this, FilterService::class.java).putExtra("color", tint).putExtra("strength", strengthValue)); toggle.text="TURN FILTER OFF"; status.text="Filter is active across your screen" }
        else { stopService(Intent(this, FilterService::class.java)); toggle.text="TURN FILTER ON"; status.text="Your screen is currently normal" }
    }
    private fun updateOverlay() { if(enabled && Settings.canDrawOverlays(this)) startService(Intent(this, FilterService::class.java).putExtra("color", tint).putExtra("strength", strengthValue)) }
    override fun onResume() { super.onResume(); if(enabled && Settings.canDrawOverlays(this)) updateOverlay() }
}
