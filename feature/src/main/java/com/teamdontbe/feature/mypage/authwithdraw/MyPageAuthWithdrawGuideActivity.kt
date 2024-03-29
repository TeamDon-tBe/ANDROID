package com.teamdontbe.feature.mypage.authwithdraw

import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.statusBarColorOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityMyPageAuthWithdrawGuideBinding
import com.teamdontbe.feature.dialog.DeleteWithTitleWideDialogFragment
import com.teamdontbe.feature.util.DialogTag.DELETE_AUTH
import com.teamdontbe.feature.util.KeyStorage.WITHDRAW_REASON
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyPageAuthWithdrawGuideActivity :
    BindingActivity<ActivityMyPageAuthWithdrawGuideBinding>(R.layout.activity_my_page_auth_withdraw_guide) {
    private val withdrawViewModel by viewModels<MyPageAuthWithdrawViewModel>()

    override fun initView() {
        statusBarColorOf(R.color.gray_1)
        binding.appbarMyPageAuthWithdrawGuide.tvAppbarTitle.setText(R.string.my_page_auth_info_withdraw_content)

        initNickname()
        initBackBtnClickListener()
        initCheckBoxClickListener()
    }

    private fun initNickname() {
        binding.tvMyPageAuthWithdrawGuideImage.text =
            getString(R.string.my_page_auth_withdraw_guide_image_1) + " " + withdrawViewModel.getNickName() +
            getString(
                R.string.my_page_auth_withdraw_guide_image_2,
            )
    }

    private fun initCheckBoxClickListener() {
        with(binding) {
            cbMyPageAuthWithdrawGuide.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    btnMyPageAuthWithdrawGuideDelete.isEnabled = true
                    btnMyPageAuthWithdrawGuideDelete.setTextColor(resources.getColor(R.color.white))

                    initDeleteBtnClickListener()
                } else {
                    btnMyPageAuthWithdrawGuideDelete.isEnabled = false
                    btnMyPageAuthWithdrawGuideDelete.setTextColor(resources.getColor(R.color.gray_9))
                }
            }
        }
    }

    private fun initDeleteBtnClickListener() {
        binding.btnMyPageAuthWithdrawGuideDelete.setOnClickListener {
            val selectedReason = intent.getStringExtra(WITHDRAW_REASON)
            Timber.tag("radioBtn on guide").i(selectedReason)

            val dialog =
                DeleteWithTitleWideDialogFragment(
                    getString(R.string.my_page_auth_info_withdraw_content),
                    getString(R.string.my_page_auth_withdraw_guide_dialog_content),
                    false,
                    selectedReason!!,
                )
            dialog.show(supportFragmentManager, DELETE_AUTH)
        }
    }

    private fun initBackBtnClickListener() {
        binding.appbarMyPageAuthWithdrawGuide.btnAppbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
