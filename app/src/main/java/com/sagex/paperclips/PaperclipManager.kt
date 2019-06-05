package com.sagex.paperclips

import android.view.View
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.roundToInt

class PaperclipManager(view: View) {
    var count = 0
        private set
    var funds = 0.0
        private set

    private var seconds = 0
    private var unsoldCount = 0
    private var ppc = 0.15              /* price per clip */
    private var pd = 0.0                /* public demand */
    private var marketingLevel = 1.0

    private val countText = view.findViewById<TextView>(R.id.paperclipCountText)
    private val unsoldText = view.findViewById<TextView>(R.id.unsoldInventoryText)
    private val ppcText = view.findViewById<TextView>(R.id.ppcText)
    private val pdText = view.findViewById<TextView>(R.id.pdText)
    private val fundsText = view.findViewById<TextView>(R.id.fundsText)

    init {
        changeUnsold(0)
        changeFunds(0.0)
        changePPC(0.0)
    }

    /* Called every second */
    fun step() {
        seconds += 1
        purchase(numberToPurchase())
    }

    fun createPaperclip() {
        addCount()
        changeUnsold(1)
    }

    fun changePPC(change: Double) {
        ppc = ((ppc + change) * 100).roundToInt() / 100.0
        ppc = maxOf(ppc, 0.01)

        pd = minOf(1.1.pow(marketingLevel - 1) * (0.08 / ppc), 300.0)

        ppcText.text = " Price per Clip: \$ ${"%.2f".format(ppc)}"
        pdText.text = "Public Demand: ${(pd * 100).roundToInt()}%"
    }

    /* Number of paperclips to purchase this second based on public demand */
    private fun numberToPurchase(): Int {
        var quantity = 0

        /* probability transformation*/
        var prob = 0.0414712 * 2.7182818284590452.pow(3.87633 * pd)

        do {
            if (Math.random() < prob) {
                quantity += 1
            }
            prob -= 0.2
        } while (prob > 0 && (Math.random() < prob))

        return maxOf(1, count / 500) * quantity
    }

    private fun purchase(quantity: Int) {
        val q = minOf(quantity, unsoldCount)
        changeUnsold(-q)
        changeFunds(q * ppc)
    }

    private fun changeUnsold(c: Int) {
        unsoldCount += c
        unsoldText.text = "Unsold Inventory: $unsoldCount"
    }

    private fun changeFunds(f: Double) {
        funds += f
        fundsText.text = "Available Funds: \$ ${"%.2f".format(funds)}"
    }

    private fun addCount() {
        count++
        countText.text = "Paperclips: $count"
    }

}