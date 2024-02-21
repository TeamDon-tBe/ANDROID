package com.teamdontbe.domain.repository

interface UserInfoRepository {
    fun saveAccessToken(accessToken: String)

    fun getAccessToken(): String

    fun saveRefreshToken(refreshToken: String)

    fun getRefreshToken(): String

    fun checkLogin(): Boolean

    fun saveCheckLogin(checkLogin: Boolean)

    fun getMemberId(): Int

    fun saveMemberId(memberId: Int)

    fun getNickName(): String

    fun saveNickName(nickName: String)

    fun getMemberProfileUrl(): String

    fun saveMemberProfileUrl(memberUrl: String)

    fun getIsNewUser(): Boolean

    fun saveIsNewUser(isNewUser: Boolean)

    fun getCheckOnboarding(): Boolean

    fun saveCheckOnboarding(checkOnboarding: Boolean)
}
