package com.teamdontbe.feature.onboarding

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingFourthBinding
import timber.log.Timber

class OnboardingFourthFragment(private val click: (String) -> Unit = { _ -> }) :
    BindingFragment<ItemOnboardingFourthBinding>(R.layout.item_onboarding_fourth) {
    override fun initView() {
        Timber.d("온보딩 네번째")
        binding.tvOnboardingFourthNickname.text = "돈비 사랑해"
        binding.ivOnboardingFourthProfile.setImageDrawable(drawableOf(R.drawable.img_posting_profile))
        hideKeyboard()
        setIntroductionData()
    }

    private fun hideKeyboard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }

    private fun setIntroductionData() {
        Timber.d(binding.etOnboardingFourthIntroduction.text.toString())
        click(binding.etOnboardingFourthIntroduction.text.toString())
    }
}
