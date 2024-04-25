package com.teamdontbe.core_ui.view

import android.content.Context
import android.text.Spannable
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.URLSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NoUnderlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

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

    private class URLSpanNoUnderline(url: String) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}
