package com.teamdontbe.data.interceptor

import android.app.Application
import android.content.Intent
import com.teamdontbe.data.BuildConfig
import com.teamdontbe.data.datasource.SharedPreferenceDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseRefreshAccessTokenDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TokenInterceptor
    @Inject
    constructor(
        private val userInfoDataSource: SharedPreferenceDataSource,
        private val json: Json,
        private val context: Application,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var accessToken = userInfoDataSource.accessToken
            var refreshToken = userInfoDataSource.refreshToken

            // 기존 request
            val request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
            val response = chain.proceed(request)

            when (response.code) {
                // 기존 request가 401 : access token 이상
                401 -> {
                    response.close()

                    val accessTokenRequest =
                        chain.request().newBuilder().get()
                            .url("${BuildConfig.DONTBE_BASE_URL}/api/v1/token")
                            .post("".toRequestBody(null))
                            .addHeader("Authorization", "Bearer $accessToken")
                            .addHeader("Refresh", "Bearer $refreshToken")
                            .build()

                    val refreshAccessTokenResponse = chain.proceed(accessTokenRequest)

                    when (refreshAccessTokenResponse.message) {
                        "유효하지 않은 토큰입니다." -> {
                            val refreshAccessToken =
                                json.decodeFromString<BaseResponse<ResponseRefreshAccessTokenDto>>(
                                    refreshAccessTokenResponse.body?.string()
                                        ?: throw IllegalStateException("\"refreshTokenResponse is null $refreshAccessTokenResponse\""),
                                )

                            userInfoDataSource.accessToken = refreshAccessToken.data?.accessToken

                            refreshAccessTokenResponse.close()

                            val newRequest =
                                chain.request().newBuilder().addHeader(
                                    "Authorization",
                                    "Bearer ${userInfoDataSource.accessToken}",
                                ).build()
                            return chain.proceed(newRequest)
                        }

//                    "access, refreshToken 모두 만료되었습니다. 재로그인이 필요합니다." -> {
//                        with(context) {
//                            CoroutineScope(Dispatchers.Main).launch {
//                                startActivity(Intent(context, LoginActivity::class.java))
//
//                                userInfoDataSource.clear()
//                            }
//                        }
//                    }

                        else -> {
                            with(context) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    startActivity(
                                        Intent.makeRestartActivityTask(
                                            packageManager.getLaunchIntentForPackage(packageName)?.component,
                                        ),
                                    )
                                    userInfoDataSource.clear()
                                }
                            }
                        }
                    }
                }
            }
            return response
        }
    }
