package com.teamdontbe.core_ui.view

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

fun TextView.setTextColorAsLinearGradient(colors: Array<Int>) {
    if (colors.isEmpty()) {
        return
    }

    setTextColor(colors[0])
    this.paint.shader =
        LinearGradient(
            0f,
            0f,
            500f,
            0f,
            colors.toIntArray(),
            arrayOf(0f, 1f).toFloatArray(),
            Shader.TileMode.CLAMP,
        )
}
