package com.teamdontbe.feature.onboarding

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingThirdBinding
import timber.log.Timber

class OnboardingThirdFragment :
    BindingFragment<ItemOnboardingThirdBinding>(R.layout.item_onboarding_third) {
    override fun initView() {
        Timber.d("온보딩 세번째")
    }
}
