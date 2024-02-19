package com.teamdontbe.feature.mypage.authwithdraw

import android.content.Intent
import android.graphics.Color
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.statusBarColorOf
import com.teamdontbe.core_ui.util.context.toast
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityMyPageAuthWithdrawReasonBinding

class MyPageAuthWithdrawReasonActivity :
    BindingActivity<ActivityMyPageAuthWithdrawReasonBinding>(R.layout.activity_my_page_auth_withdraw_reason) {
    override fun initView() {
        statusBarColorOf(R.color.gray_1)
        binding.appbarMyPageAuthWithdrawReason.tvAppbarTitle.setText(R.string.my_page_auth_info_withdraw_content)

        onRadioButtonClicked()
        initBackBtnClickListener()
    }

    private fun onRadioButtonClicked() {
        with(binding) {
            rgMyPageAuthWithdrawReasonContent.setOnCheckedChangeListener { group, checkedId ->
                btnMyPageAuthWithdrawReasonNext.isEnabled = true
                btnMyPageAuthWithdrawReasonNext.setTextColor(Color.WHITE)

                when (checkedId) {
                    R.id.rb_my_page_auth_withdraw_reason_content_1 -> {
                        toast("1")
                    }

                    R.id.rb_my_page_auth_withdraw_reason_content_2 -> {
                        toast("2")
                    }

                    R.id.rb_my_page_auth_withdraw_reason_content_3 -> {
                        toast("3")
                    }

                    R.id.rb_my_page_auth_withdraw_reason_content_4 -> {
                        toast("4")
                    }

                    R.id.rb_my_page_auth_withdraw_reason_content_5 -> {
                        toast("5")
                    }

                    R.id.rb_my_page_auth_withdraw_reason_content_6 -> {
                        toast("6")
                    }

                    R.id.rb_my_page_auth_withdraw_reason_content_7 -> {
                        toast("7")
                    }
                }
                initNextBtnClickListener()
            }
        }
    }

    private fun initBackBtnClickListener() {
        binding.appbarMyPageAuthWithdrawReason.btnAppbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initNextBtnClickListener() {
        binding.btnMyPageAuthWithdrawReasonNext.setOnClickListener {
            startActivity(Intent(this, MyPageAuthWithdrawGuideActivity::class.java))
        }
    }
}
