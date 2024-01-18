package com.teamdontbe.feature

import android.app.Activity
import android.content.Intent
import android.os.Handler
import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.databinding.ActivitySplashBinding
import com.teamdontbe.feature.login.LoginActivity
import com.teamdontbe.feature.login.LoginViewModel
import com.teamdontbe.feature.signup.SignUpAgreeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun initView() {
        initSplash()
    }

    private fun initSplash() {
        // 타이머가 끝나면 내부 실행
        Handler().postDelayed(
            Runnable {
                if (loginViewModel.getNickName().isNotBlank() && loginViewModel.checkLogin()) {
                    navigateTo<MainActivity>()
                } else if (loginViewModel.getNickName().isBlank()) {
                    navigateTo<SignUpAgreeActivity>()
                } else {
                    navigateTo<LoginActivity>()
                }
                finish()
            },
            3000,
        ) // 3초
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this@SplashActivity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}
