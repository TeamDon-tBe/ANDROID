package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import com.teamdontbe.domain.repository.UserInfoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepositoryImpl
@Inject
constructor(
    private val sharedPreferenceDataSource: SharedPreferenceDataSource,
) : UserInfoRepository {
    override fun saveAccessToken(accessToken: String) {
        sharedPreferenceDataSource.accessToken = accessToken
    }

    override fun getAccessToken(): String {
        return sharedPreferenceDataSource.accessToken ?: ""
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferenceDataSource.refreshToken = refreshToken
    }

    override fun getRefreshToken(): String {
        return sharedPreferenceDataSource.refreshToken ?: ""
    }

    override fun checkLogin(): Boolean {
        return sharedPreferenceDataSource.checkLogin
    }

    override fun saveCheckLogin(checkLogin: Boolean) {
        sharedPreferenceDataSource.checkLogin = checkLogin
    }

    override fun getMemberId(): Int {
        return sharedPreferenceDataSource.memberId
    }

    override fun saveMemberId(memberId: Int) {
        sharedPreferenceDataSource.memberId = memberId
    }

    override fun getNickName(): String {
        return sharedPreferenceDataSource.nickName ?: ""
    }

    override fun saveNickName(nickName: String) {
        sharedPreferenceDataSource.nickName = nickName
    }

    override fun getMemberProfileUrl(): String {
        return sharedPreferenceDataSource.memberProfileUrl ?: ""
    }

    override fun saveMemberProfileUrl(memberUrl: String) {
        sharedPreferenceDataSource.memberProfileUrl = memberUrl
    }

    override fun getIsNewUser(): Boolean {
        return sharedPreferenceDataSource.isNewUser
    }

    override fun saveIsNewUser(isNewUser: Boolean) {
        sharedPreferenceDataSource.isNewUser = isNewUser
    }

    override fun getCheckOnboarding(): Boolean {
        return sharedPreferenceDataSource.isOnboardingFirst
    }

    override fun saveCheckOnboarding(checkOnboarding: Boolean) {
        sharedPreferenceDataSource.isOnboardingFirst = checkOnboarding
    }

    override fun clear() {
        sharedPreferenceDataSource.clear()
    }
}
