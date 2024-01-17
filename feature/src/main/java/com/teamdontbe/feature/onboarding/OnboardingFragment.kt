package com.teamdontbe.feature.onboarding

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentOnboardingBinding
import com.teamdontbe.feature.posting.PostingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnboardingFragment :
    BindingFragment<FragmentOnboardingBinding>(R.layout.fragment_onboarding) {
    private val postingViewModel by activityViewModels<PostingViewModel>()
    private var _onboardingAdapter: OnboardingAdapter? = null
    private val onboardingAdapter
        get() = requireNotNull(_onboardingAdapter) { "adapter 초기화 안됨" }

    override fun initView() {
        initOnboardingAdapter()
        initBtnOnboardingNextClickListener()
        initIvOnboardingBackClickListener()
        initObserve()
        initSkipTextClickListener()
    }

    private fun initObserve() {
        postingViewModel.postPosting.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> navigateToHomeFragment()
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        postingViewModel.introduction.observe(viewLifecycleOwner) {
            initBtnOnboardingStartClickListener(it)
        }
    }

    private val pageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.ivOnboardingBack.visibility =
                    if (position == 0) View.INVISIBLE else View.VISIBLE
                binding.btnOnboardingNext.isVisible = position != 3
                binding.btnOnboardingStart.isVisible = position == 3
                binding.tvOnboardingSkip.text =
                    if (position == 3) {
                        getString(R.string.tv_onboarding_skip_introduction)
                    } else {
                        getString(
                            R.string.tv_onboarding_skip,
                        )
                    }
            }
        }

    private fun initOnboardingAdapter() {
        _onboardingAdapter =
            OnboardingAdapter(this)
        binding.vpOnboarding.adapter = _onboardingAdapter
        binding.vpOnboarding.isUserInputEnabled = false
        binding.vpOnboarding.registerOnPageChangeCallback(pageChangeCallback)
        TabLayoutMediator(binding.tabOnboardingIndicator, binding.vpOnboarding) { _, _ ->
        }.attach()
    }

    private fun initBtnOnboardingNextClickListener() {
        binding.btnOnboardingNext.setOnClickListener {
            if (binding.vpOnboarding.currentItem + 1 < (_onboardingAdapter?.itemCount ?: 0)) {
                binding.vpOnboarding.setCurrentItem(binding.vpOnboarding.currentItem + 1, true)
            }
        }
    }

    private fun initIvOnboardingBackClickListener() {
        binding.ivOnboardingBack.setOnClickListener {
            if (-1 < binding.vpOnboarding.currentItem - 1) {
                binding.vpOnboarding.setCurrentItem(binding.vpOnboarding.currentItem - 1, true)
            }
        }
    }

    private fun initBtnOnboardingStartClickListener(introduction: String) {
        binding.btnOnboardingStart.setOnClickListener {
            postingViewModel.posting(introduction)
        }
    }

    private fun initSkipTextClickListener() {
        binding.tvOnboardingSkip.setOnClickListener {
            if (binding.tvOnboardingSkip.text == getString(R.string.tv_onboarding_skip_introduction)) {
                navigateToHomeFragment()
            } else {
                binding.vpOnboarding.setCurrentItem(
                    3,
                    true,
                )
            }
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            R.id.action_onboarding_to_home,
        )
    }

    override fun onDestroyView() {
        _onboardingAdapter = null
        super.onDestroyView()
    }
}
