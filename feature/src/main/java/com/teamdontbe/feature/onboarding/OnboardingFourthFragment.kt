package com.teamdontbe.feature.onboarding

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingFourthBinding
import timber.log.Timber

class OnboardingFourthFragment :
    BindingFragment<ItemOnboardingFourthBinding>(R.layout.item_onboarding_fourth) {
    override fun initView() {
        Timber.d("온보딩 네번째")
    }
}
