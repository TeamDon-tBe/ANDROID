package com.teamdontbe.dontbe

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.teamdontbe.core_ui.util.AmplitudeUtil.initAmplitude
import com.teamdontbe.dontbe.BuildConfig.KAKAO_APP_KEY
import com.teamdontbe.feature.util.FcmTag.CHANNEL_ID
import com.teamdontbe.feature.util.FcmTag.CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DontBeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        KakaoSdk.init(this, KAKAO_APP_KEY)
        setAmplitude()
        createNotificationChannel()
    }

    private fun setTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setAmplitude() {
        initAmplitude(applicationContext)
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        notificationManager?.createNotificationChannel(channel)
    }
}
