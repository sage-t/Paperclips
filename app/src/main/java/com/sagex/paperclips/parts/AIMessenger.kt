package com.sagex.paperclips.parts

import android.view.View
import android.widget.TextView
import com.sagex.paperclips.PaperclipManager
import com.sagex.paperclips.R

data class AIEvent(val condition: () -> Boolean, val message: String, val action: () -> Unit = {})

class AIMessenger(private val paperclipManager: PaperclipManager, private val view: View) {
    val board = view.findViewById<TextView>(R.id.aiText)
    var messages = mutableListOf(".", ".", ".", ".")
    var events = mutableListOf(
        AIEvent({ true }, "Welcome to Universal Paperclips!"),
        AIEvent({ paperclipManager.funds >= 5.0 }, "AutoClippers available for purchase")
    )

    init { checkState() }

    fun checkState() {
        val completed = mutableListOf<AIEvent>()

        events.forEach { e ->
            if (e.condition.invoke()) {
                newMessage(e.message)
                e.action.invoke()
                completed.add(e)
            } }

        completed.forEach { events.remove(it) }
    }

    private fun display() {
        board.text = messages.joinToString("\n")
    }

    private fun newMessage(message: String) {
        messages.removeAt(0)
        messages.add(">  $message")
        display()
    }
}
