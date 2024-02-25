package com.teamdontbe.feature.mypage.authwithdraw

import android.content.Intent
import android.graphics.Color
import android.widget.RadioButton
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.statusBarColorOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityMyPageAuthWithdrawReasonBinding
import com.teamdontbe.feature.util.KeyStorage.WITHDRAW_REASON
import timber.log.Timber

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

                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                val selectedText = selectedRadioButton.text.toString()
                Timber.tag("radioBtn on reason").i(selectedText)

                initNextBtnClickListener(selectedText)
            }
        }
    }

    private fun initBackBtnClickListener() {
        binding.appbarMyPageAuthWithdrawReason.btnAppbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initNextBtnClickListener(selectedReason: String) {
        binding.btnMyPageAuthWithdrawReasonNext.setOnClickListener {
            val intent = Intent(this, MyPageAuthWithdrawGuideActivity::class.java).apply {
                putExtra(WITHDRAW_REASON, selectedReason)
            }
            startActivity(intent)
        }
    }
}
