package com.teamdontbe.feature.posting

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class AnimateProgressBar(
    private val progressBar: ProgressBar,
    private val from: Float,
    private val to: Float,
) : Animation() {
    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation,
    ) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + (to - from) * interpolatedTime
        progressBar.progress = value.toInt()
    }
}
