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
    }
