package com.teamdontbe.data.datasource

interface SharedPreferenceDataSource {
    var accessToken: String?
    var refreshToken: String?
    var checkLogin: Boolean
    var memberId: Int
    var nickName: String?
    var memberProfileUrl: String?
    var isNewUser: Boolean
    var isOnboardingFirst: Boolean
    var isPushAlarmAllowed: Boolean?
    var fcmToken: String?

    fun clear()
    fun clearForRefreshToken()
}
