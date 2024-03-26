package com.teamdontbe.core_ui.util

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.DefaultTrackingOptions
import com.teamdontbe.core_ui.BuildConfig.AMPLITUDE_API_KEY

object AmplitudeUtil {
    private lateinit var amplitude: Amplitude

    fun initAmplitude(applicationContext: Context) {
        amplitude = Amplitude(
            Configuration(
                apiKey = AMPLITUDE_API_KEY,
                context = applicationContext,
                defaultTracking = DefaultTrackingOptions.NONE
            )
        )
    }
}
