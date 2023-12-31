package com.teamdontbe.feature.login

import android.content.ContentValues
import android.content.Intent
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityLoginBinding
import timber.log.Timber


class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            val errorMessage =
                when (error.toString()) {
                    AuthErrorCause.AccessDenied.toString() -> "접근이 거부 됨(동의 취소)"
                    AuthErrorCause.InvalidClient.toString() -> "유효하지 않은 앱"
                    AuthErrorCause.InvalidGrant.toString() -> "인증 수단이 유효하지 않아 인증할 수 없는 상태"
                    AuthErrorCause.InvalidRequest.toString() -> "요청 파라미터 오류"
                    AuthErrorCause.InvalidScope.toString() -> "유효하지 않은 scope ID"
                    AuthErrorCause.Misconfigured.toString() -> "설정이 올바르지 않음(android key hash)"
                    AuthErrorCause.ServerError.toString() -> "서버 내부 에러"
                    AuthErrorCause.Unauthorized.toString() -> "앱이 요청 권한이 없음"
                    else -> "기타 에러"
                }
            Timber.e(ContentValues.TAG, errorMessage)
        } else if (token != null) {
            Timber.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            navigateToMainActivity()
        }
    }

    override fun initView() {
        initKeyHash()
        initKakaoLoginBtnClickListener()
    }

    private fun initKakaoLoginBtnClickListener() {
        // 버튼 클릭했을 때 로그인
        with(binding) {
            btnLoginKakao.setOnClickListener {
                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                        when {
                            error != null -> {
                                Timber.tag("kakao").e(error, "카카오톡으로 로그인 실패")

                                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                                // 의도적으로 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리(ex. 뒤로가기)
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    return@loginWithKakaoTalk
                                }
                                setKakaoCallback()
                            }

                            token != null -> {
                                Timber.tag("kakao").i("카카오톡으로 로그인 성공 %s", token.accessToken)
                                navigateToMainActivity()
                            }
                        }
                    }
                } else {
                    setKakaoCallback()
                }
            }
        }
    }

    private fun setKakaoCallback() {
        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
        UserApiClient.instance.loginWithKakaoAccount(
            this@LoginActivity,
            callback = callback
        )
    }

    private fun initKeyHash() {
        // 해시키 구하기
        val keyHash = Utility.getKeyHash(this)
        Timber.tag("Hash").d(keyHash)
    }

    private fun navigateToMainActivity() {
        // 로그인 -> 성공 화면(원래는 온보딩 화면으로 넘어가야 함)
        startActivity(Intent(this, MainActivity::class.java))
    }
}