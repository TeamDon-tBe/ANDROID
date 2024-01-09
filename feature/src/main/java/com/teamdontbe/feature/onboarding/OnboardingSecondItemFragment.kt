package com.teamdontbe.feature.onboarding

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingSecondBinding
import timber.log.Timber

class OnboardingSecondItemFragment :
    BindingFragment<ItemOnboardingSecondBinding>(R.layout.item_onboarding_second) {
    override fun initView() {
        Timber.d("온보딩 두번째")
    }
}
