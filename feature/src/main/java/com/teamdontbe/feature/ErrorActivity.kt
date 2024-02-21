package com.teamdontbe.feature

import android.content.Context
import android.content.Intent
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.databinding.ActivityErrorBinding

class ErrorActivity : BindingActivity<ActivityErrorBinding>(R.layout.activity_error) {
    override fun initView() {
        binding.btnErrorToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    companion object {
        fun navigateToErrorPage(context: Context) {
            context.startActivity(Intent(context, ErrorActivity::class.java))
        }
    }
}
