package com.teamdontbe.feature.onboarding

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.intent.getParcelable
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentOnboardingBinding
import com.teamdontbe.feature.signup.SignUpAgreeActivity
import com.teamdontbe.feature.signup.UserProfileModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFragment :
    BindingFragment<FragmentOnboardingBinding>(R.layout.fragment_onboarding) {
    private val onboardingViewModel by activityViewModels<OnboardingViewModel>()
    private var _onboardingAdapter: OnboardingAdapter? = null
    private val onboardingAdapter
        get() = requireNotNull(_onboardingAdapter) { "adapter 초기화 안됨" }

    private var signUpAgree: UserProfileModel? = null

    override fun initView() {
        binding.vm = onboardingViewModel

        checkIsNewUser()
        initOnboardingAdapter()
        initBtnOnboardingNextClickListener()
        initIvOnboardingBackClickListener()
        initObserve()
        initSkipTextClickListener()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        signUpAgree =
            requireActivity().intent.getParcelable(
                SignUpAgreeActivity.SIGN_UP_AGREE,
                UserProfileModel::class.java,
            )

        Timber.tag("my_page").d("my page에서 받아오는 sign up agree on attach : $signUpAgree")
    }

    private fun checkIsNewUser() {
        binding.tvOnboardingSkip.isVisible =
            !(onboardingViewModel.getIsNewUser()) || onboardingViewModel.getCheckOnboarding()
    }

    private fun initObserve() {
        onboardingViewModel.postPosting.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    navigateToHomeFragment()
                    onboardingViewModel.saveCheckLogin(true)
                    onboardingViewModel.saveCheckOnboarding(true)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        onboardingViewModel.introduction.observe(viewLifecycleOwner) {
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
                binding.btnOnboardingNext.visibility =
                    if (position == 3) View.INVISIBLE else View.VISIBLE
                binding.btnOnboardingStart.isVisible = position == 3
                binding.tvOnboardingSkip.text =
                    if (position == 3) {
                        getString(R.string.tv_onboarding_skip_introduction)
                    } else {
                        getString(
                            R.string.tv_onboarding_skip,
                        )
                    }
                checkIsNewUser()
            }
        }

    private fun initOnboardingAdapter() {
        _onboardingAdapter = OnboardingAdapter(this)
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
            val inputNickName = onboardingViewModel.getNickName()

            onboardingViewModel.posting(introduction)
            onboardingViewModel.patchUserProfileEdit(
                inputNickName,
                null,
                introduction,
                null,
            )
        }
    }

    private fun initSkipTextClickListener() {
        binding.tvOnboardingSkip.setOnClickListener {
            if (binding.tvOnboardingSkip.text == getString(R.string.tv_onboarding_skip_introduction)) {
                navigateToHomeFragment()
                onboardingViewModel.saveCheckLogin(true)
                onboardingViewModel.saveCheckOnboarding(true)
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
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onDestroyView()
    }
}
