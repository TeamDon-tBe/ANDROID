package com.teamdontbe.core_ui.view

import android.annotation.SuppressLint
import android.view.MotionEvent
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

// 0.5초 이하 이벤트만 처리해주는 클릭 리스너
@SuppressLint("ClickableViewAccessibility")
fun View.setOnShortClickListener(timeThreshold: Long = 500, action: () -> Unit) {
    var lastClickTime: Long = 0

    this.setOnTouchListener { _, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                lastClickTime = System.currentTimeMillis()
            }

            MotionEvent.ACTION_UP -> {
                val downTime = lastClickTime
                val upTime = System.currentTimeMillis()
                val duration = upTime - downTime
                if (duration < timeThreshold) {
                    action()
                }
            }
        }
        false
    }
}
