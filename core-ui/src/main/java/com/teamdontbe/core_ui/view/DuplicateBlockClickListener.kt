package com.teamdontbe.core_ui.view

import android.view.View

fun View.setOnDuplicateBlockClick(click: (View) -> Unit) {
    val listener = DuplicateBlockClickListener { click(it) }
    setOnClickListener(listener)
}

class DuplicateBlockClickListener(private val click: (View) -> Unit) : View.OnClickListener {
    companion object {
        private const val CLICK_INTERVAL = 1000
    }

    private var lastClickedTime: Long = 0L

    override fun onClick(v: View?) {
        if (isSafe() && v != null) {
            lastClickedTime = System.currentTimeMillis()
            click(v)
        }
    }

    private fun isSafe(): Boolean {
        return System.currentTimeMillis() - lastClickedTime > CLICK_INTERVAL
    }
}