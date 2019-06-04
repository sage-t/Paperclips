package com.sagex.paperclips.parts

import android.view.View
import android.widget.TextView
import com.sagex.paperclips.PaperclipManager
import com.sagex.paperclips.R

data class AIEvent(val c: () -> Boolean, val m: String, val action: () -> Unit = {})

class AIMessenger(val paperclipManager: PaperclipManager, private val view: View) {
    val board = view.findViewById<TextView>(R.id.aiText)
    var messages = mutableListOf(".", ".", ".", ">  Welcome to Universal Paperclips!")
    var events = mutableListOf<AIEvent>(
        AIEvent({ paperclipManager.funds > 5.0 }, "AutoClippers available for purchase")
    )

    init {
        display()
    }

    fun checkState() {
        events.forEach { if (it.c.invoke()) {
            newMessage(it.m)

        } }
    }

    fun display() {
        board.text = messages.joinToString("\n")
    }

    fun newMessage(message: String) {
        messages.removeAt(0)
        messages.add(">  $message")
        display()
    }
}
