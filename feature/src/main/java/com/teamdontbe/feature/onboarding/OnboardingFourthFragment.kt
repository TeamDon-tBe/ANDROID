package com.teamdontbe.feature.onboarding

import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingFourthBinding
import com.teamdontbe.feature.posting.PostingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFourthFragment() :
    BindingFragment<ItemOnboardingFourthBinding>(R.layout.item_onboarding_fourth) {
    private val postingViewModel by activityViewModels<PostingViewModel>()

    override fun initView() {
        binding.vm = postingViewModel
        Timber.d("온보딩 네번째")
        binding.tvOnboardingFourthNickname.text = "돈비 사랑해"
        binding.ivOnboardingFourthProfile.setImageDrawable(drawableOf(R.drawable.img_posting_profile))
        hideKeyboard()
    }

    private fun hideKeyboard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }
}
