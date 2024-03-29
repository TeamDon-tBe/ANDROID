package com.teamdontbe.dontbe

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.teamdontbe.dontbe.BuildConfig.KAKAO_APP_KEY
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DontBeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        KakaoSdk.init(this, KAKAO_APP_KEY)
    }

    private fun setTimber() {
        Timber.plant(Timber.DebugTree())
    }
}