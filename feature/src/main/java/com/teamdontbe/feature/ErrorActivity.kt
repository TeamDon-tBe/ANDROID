package com.teamdontbe.feature

import android.content.Context
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.intent.navigateTo
import com.teamdontbe.feature.databinding.ActivityErrorBinding

class ErrorActivity : BindingActivity<ActivityErrorBinding>(R.layout.activity_error) {
    override fun initView() {
        binding.btnErrorToHome.setOnClickListener {
            navigateTo<MainActivity>(this)
        }
    }

    companion object {
        fun navigateToErrorPage(context: Context) {
            navigateTo<ErrorActivity>(context)
        }
    }
}
