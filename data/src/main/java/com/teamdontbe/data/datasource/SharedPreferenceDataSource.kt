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

    fun clear()
    fun clearForRefreshToken()
}
