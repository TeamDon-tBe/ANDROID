package com.teamdontbe.feature.mypage.authinfo

import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.navigateToAppSettings
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityPushAlarmBinding

class MyPagePushAlarmActivity :
    BindingActivity<ActivityPushAlarmBinding>(R.layout.activity_push_alarm) {
    override fun initView() {
        binding.layoutPushAlarmAppbar.tvAppbarTitle.text =
            getString(R.string.tv_push_alarm_appbar_title)
        initPushAlarmSettingClickListener()
        initBackBtnClickListener()
    }

    private fun initPushAlarmSettingClickListener() {
        binding.tvPushAlarmSetting.setOnClickListener {
            navigateToAppSettings()
        }
        binding.ivPushAlarmSetting.setOnClickListener {
            navigateToAppSettings()
        }
    }

    private fun initBackBtnClickListener() {
        binding.layoutPushAlarmAppbar.btnAppbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
