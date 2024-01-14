package com.teamdontbe.data_local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceDataSourceImpl
    @Inject
    constructor(
        private val sharedPreferences: SharedPreferences,
    ) : SharedPreferenceDataSource {
        override var accessToken: String?
            get() = sharedPreferences.getString("AccessToken", null)
            set(value) = sharedPreferences.edit { putString("AccessToken", value) }

        override var refreshToken: String?
            get() = sharedPreferences.getString("AccessToken", null)
            set(value) = sharedPreferences.edit { putString("AccessToken", value) }
        override var checkLogin: Boolean
            get() = sharedPreferences.getBoolean("AccessToken", false)
            set(value) = sharedPreferences.edit { putBoolean("AccessToken", value) }
        override var memberId: Int
            get() = sharedPreferences.getInt("AccessToken", -1)
            set(value) = sharedPreferences.edit { putInt("AccessToken", value) }
        override var nickName: String?
            get() = sharedPreferences.getString("AccessToken", null)
            set(value) = sharedPreferences.edit { putString("AccessToken", value) }
    }
