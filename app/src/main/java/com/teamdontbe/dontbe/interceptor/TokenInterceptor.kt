package com.teamdontbe.dontbe.interceptor

import android.app.Application
import android.content.Intent
import com.teamdontbe.core_ui.util.context.toast
import com.teamdontbe.data.BuildConfig
import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseRefreshAccessTokenDto
import com.teamdontbe.feature.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class TokenInterceptor
    @Inject
    constructor(
        private val userInfoDataSource: SharedPreferenceDataSource,
        private val json: Json,
        private val context: Application,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var accessToken = userInfoDataSource.accessToken
            var refreshToken = userInfoDataSource.refreshToken

            // 기존 request
            val request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
            val response = chain.proceed(request)
            Timber.tag("interceptor").d(accessToken)
            Timber.tag("interceptor").d(refreshToken)

            when (response.code) {
                // 기존 request가 401 : access token 이상
                401 -> {
                    Timber.d("access token 만료됨!!!")

                    response.close()
                    // access token 재발급 request
                    val accessTokenRequest =
                        chain.request().newBuilder()
                            .url("${BuildConfig.DONTBE_BASE_URL}/api/v1/auth/token")
                            .get()
                            .addHeader("Authorization", "Bearer $accessToken")
                            .addHeader("Refresh", "Bearer $refreshToken")
                            .build()

                    val refreshAccessTokenResponse = chain.proceed(accessTokenRequest)

                    val refreshAccessToken =
                        json.decodeFromString<BaseResponse<ResponseRefreshAccessTokenDto>>(
                            refreshAccessTokenResponse.body?.string()
                                ?: throw IllegalStateException("\"refreshTokenResponse is null $refreshAccessTokenResponse\""),
                        )

                    when (refreshAccessToken.message) {
                        // access token 재발급 성공
                        "토큰 재발급 성공" -> {
                            Timber.d("access token 재발급 성공!!!")
                            userInfoDataSource.accessToken = refreshAccessToken.data?.accessToken

                            refreshAccessTokenResponse.close()

                            val newRequest =
                                chain.request().newBuilder().addHeader(
                                    "Authorization",
                                    "Bearer ${userInfoDataSource.accessToken}",
                                ).build()

                            return chain.proceed(newRequest)
                        }

                        // access token, refresh token 둘 다 만료되면
                        else -> {
                            with(context) {
                                Timber.d("refresh token도 만료됨!!!")
                                refreshAccessTokenResponse.close()

                                CoroutineScope(Dispatchers.Main).launch {
                                    userInfoDataSource.clear()
                                    Timber.tag("interceptor").d(userInfoDataSource.accessToken)
                                    Timber.tag("interceptor").d(userInfoDataSource.refreshToken)
                                    toast("인증 정보가 만료되었습니다. 다시 로그인 해주세요.")
                                    val intent = Intent(this@with, LoginActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
            return response
        }
    }
