package com.sagex.paperclips

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import com.sagex.paperclips.parts.AIMessenger

class MainActivity : Activity() {

    lateinit var mainHandler: Handler
    lateinit var paperclipManager: PaperclipManager
    lateinit var ai: AIMessenger

    private val updatePaperclipManagerTask = object : Runnable {
        override fun run() {
            paperclipManager.step()
            ai.checkState()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(android.R.id.content)

        mainHandler = Handler(Looper.getMainLooper())
        paperclipManager = PaperclipManager(view)
        ai = AIMessenger(paperclipManager, view)

        view.findViewById<Button>(R.id.makePaperclipButton).setOnClickListener { paperclipManager.createPaperclip() }
        view.findViewById<Button>(R.id.lowerPriceButton).setOnClickListener { paperclipManager.changePPC(-0.01) }
        view.findViewById<Button>(R.id.raisePriceButton).setOnClickListener { paperclipManager.changePPC(0.01) }
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updatePaperclipManagerTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updatePaperclipManagerTask)
    }


}
