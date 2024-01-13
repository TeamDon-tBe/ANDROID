package com.teamdontbe.feature

import android.content.Intent
import android.os.Handler
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.databinding.ActivitySplashBinding
import com.teamdontbe.feature.login.LoginActivity

class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun initView() {
        initSplash()
    }

    private fun initSplash() {
        // 타이머가 끝나면 내부 실행
        Handler().postDelayed(
            Runnable {
                startActivity(Intent(this, LoginActivity::class.java))
                // 현재 액티비티 닫기
                finish()
            },
            3000,
        ) // 3초
    }
}
