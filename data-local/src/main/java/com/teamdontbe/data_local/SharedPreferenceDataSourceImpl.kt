package com.teamdontbe.data_local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import javax.inject.Inject

class SharedPreferenceDataSourceImpl
@Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : SharedPreferenceDataSource {
    override var accessToken: String?
        get() = sharedPreferences.getString("AccessToken", null)
        set(value) = sharedPreferences.edit { putString("AccessToken", value) }

    override var refreshToken: String?
        get() = sharedPreferences.getString("RefreshToken", null)
        set(value) = sharedPreferences.edit { putString("RefreshToken", value) }
    override var checkLogin: Boolean
        get() = sharedPreferences.getBoolean("CheckLogin", false)
        set(value) = sharedPreferences.edit { putBoolean("CheckLogin", value) }
    override var memberId: Int
        get() = sharedPreferences.getInt("MemberId", -1)
        set(value) = sharedPreferences.edit { putInt("MemberId", value) }
    override var nickName: String?
        get() = sharedPreferences.getString("NickName", null)
        set(value) = sharedPreferences.edit { putString("NickName", value) }
    override var memberProfileUrl: String?
        get() = sharedPreferences.getString("MemberProfileUrl", null)
        set(value) = sharedPreferences.edit { putString("MemberProfileUrl", value) }

    override var isNewUser: Boolean
        get() = sharedPreferences.getBoolean("IsNewUser", false)
        set(value) = sharedPreferences.edit { putBoolean("IsNewUser", value) }

    override var isOnboardingFirst: Boolean
        get() = sharedPreferences.getBoolean("IsOnboardingFirst", false)
        set(value) = sharedPreferences.edit { putBoolean("IsOnboardingFirst", value) }

    override fun clear() {
        sharedPreferences.edit {
            remove("AccessToken")
            remove("RefreshToken")
            remove("CheckLogin")
            remove("MemberId")
            remove("NickName")
            remove("MemberProfileUrl")
            remove("IsNewUser")
            remove("IsOnboardingFirst")
        }
    }

    override fun clearForRefreshToken() {
        sharedPreferences.edit {
            remove("AccessToken")
            remove("RefreshToken")
            remove("CheckLogin")
        }
    }
}
