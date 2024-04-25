package com.teamdontbe.core_ui.view

import android.content.Context
import android.text.Spannable
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.MotionEvent

class NoUnderlineEditTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyle) {

    init {
        // 클릭 가능한 기능을 활성화합니다.
        isClickable = true
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        stripUnderlines()
    }

    private fun stripUnderlines() {
        val seq = text

        if (TextUtils.isEmpty(seq) || seq !is Spannable) {
            return
        }

        val spans = seq.getSpans(0, seq.length, URLSpan::class.java)
        for (span in spans) {
            val start = seq.getSpanStart(span)
            val end = seq.getSpanEnd(span)
            seq.removeSpan(span)
            val newSpan = URLSpanNoUnderline(span.url)
            seq.setSpan(newSpan, start, end, 0)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        return super.onTouchEvent(event)
    }

    private inner class URLSpanNoUnderline(url: String) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}

class NoUnderlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        stripUnderlines()
    }

    private fun stripUnderlines() {
        val seq = text

        if (TextUtils.isEmpty(seq) || seq !is Spannable) {
            return
        }

        val spans = seq.getSpans(0, seq.length, URLSpan::class.java)
        for (span in spans) {
            val start = seq.getSpanStart(span)
            val end = seq.getSpanEnd(span)
            seq.removeSpan(span)
            val newSpan = URLSpanNoUnderline(span.url)
            seq.setSpan(newSpan, start, end, 0)
        }
    }

    private inner class URLSpanNoUnderline(url: String) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}
