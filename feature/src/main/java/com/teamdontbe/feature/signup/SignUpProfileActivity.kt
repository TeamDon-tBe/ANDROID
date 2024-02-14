package com.teamdontbe.feature.signup

import android.content.Intent
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.colorOf
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.context.openKeyboard
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivitySignUpProfileBinding
import com.teamdontbe.feature.mypage.bottomsheet.MyPageBottomSheet.Companion.MY_PAGE_PROFILE
import com.teamdontbe.feature.signup.SignUpAgreeActivity.Companion.SIGN_UP_AGREE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpProfileActivity :
    BindingActivity<ActivitySignUpProfileBinding>(R.layout.activity_sign_up_profile) {
    private val viewModel by viewModels<SignUpProfileViewModel>()

    override fun initView() {
        binding.vm = viewModel

        val flag = initMyPageProfileAppBarTitle()
        initUpdateErrorMessage(flag)
        initNickNameDoubleStateObserve()
        initNextBtnStateObserve(flag)
        initBackBtnClickListener(flag)
        initKeyboardSetting()
    }

    private fun initMyPageProfileAppBarTitle(): String {
        return when {
            intent.getStringExtra(MY_PAGE_PROFILE) != null -> {
                initializeMyPageProfile()
                MY_PAGE_PROFILE
            }

            else -> {
                initializeSignUpAgree()
                SIGN_UP_AGREE
            }
        }
    }

    private fun initializeMyPageProfile() = with(binding) {
        setUpInitMyPageProfileUi()
        setUpMyPageProfileViewModelUi()
    }

    private fun setUpInitMyPageProfileUi() = with(binding) {
        val myPageAppBarTitle =
            intent.getStringExtra(MY_PAGE_PROFILE) ?: getString(R.string.my_page_nickname)
        appbarSignUp.tvAppbarTitle.text = myPageAppBarTitle
        btnSignUpAgreeNext.text = getString(R.string.my_page_profile_edit_completed)
        etSignUpProfileNickname.setText(viewModel.getUserNickName() ?: "")
    }

    private fun setUpMyPageProfileViewModelUi() {
        viewModel.apply {
            getUserProfileIntroduce()
            checkOnMyPageInitialNickName()
        }
    }

    private fun initializeSignUpAgree() {
        binding.groupSignUpIntroduce.visibility = View.INVISIBLE
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    // 닉네임 중복 체크 메세지
    private fun initUpdateErrorMessage(flag: String) {
        viewModel.isNickNameValid.observe(this) {
            val messageResId =
                if (it) R.string.sign_up_profile_check_text else R.string.sign_up_profile_correct_check
            val textColorResId = if (it) R.color.gray_8 else R.color.error

            updateAgreeText(messageResId, textColorResId)
            initDoubleBtnClickListener(flag, it)
        }
    }

    private fun updateAgreeText(
        messageResId: Int,
        textColorResId: Int,
    ) {
        binding.tvSignUpAgreeMessage.apply {
            text = context.getString(messageResId)
            setTextColor(colorOf(textColorResId))
        }
    }

    // 중복 확인 버튼 클릭
    private fun initDoubleBtnClickListener(flag: String, btnAvailable: Boolean) {
        binding.btnSignUpProfileDoubleCheck.setOnClickListener {
            viewModel.getNickNameDoubleCheck(binding.etSignUpProfileNickname.text.toString())
            handleFlag(flag, btnAvailable)
        }
    }

    // 프로필 편집, 마이페이지 프로필 편집 분기처리
    private fun handleFlag(flag: String, btnAvailable: Boolean) {
        if (btnAvailable) {
            when (flag) {
                SIGN_UP_AGREE -> hideKeyboard(binding.root)
                MY_PAGE_PROFILE -> focusOnIntroduceEditText()
            }
        }
    }

    // editText 자동 포커스
    private fun focusOnIntroduceEditText() {
        binding.etSignUpAgreeIntroduce.apply {
            requestFocus()
            setSelection(this.text.length)
        }
    }

    private fun initNickNameDoubleStateObserve() {
        viewModel.nickNameDoubleState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    if (it.data) {
                        updateErrorMessage(true)
                    } else {
                        updateErrorMessage(false)
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun nextBtnObserve(
        inputNickName: String,
        flag: String,
    ) {
        binding.btnSignUpAgreeNext.setOnClickListener {
            val allowedCheck = intent.getBooleanExtra(SIGN_UP_AGREE, false)
            viewModel.isBtnSelected.observe(this) {
                if (it) {
                    viewModel.patchUserProfileEdit(
                        inputNickName,
                        allowedCheck,
                        binding.etSignUpAgreeIntroduce.text.toString(),
                        null,
                    )
                    when (flag) {
                        MY_PAGE_PROFILE -> {
                            viewModel.setUserNickName(inputNickName)
                            finish()
                        }

                        SIGN_UP_AGREE -> {
                            viewModel.setUserNickName(inputNickName)

                            val userProfile =
                                setUpUserProfile(
                                    inputNickName,
                                    allowedCheck,
                                )
                            navigateToMainAcitivity(userProfile)
                        }
                    }
                }
            }
        }
    }

    private fun setUpUserProfile(
        inputNickName: String,
        allowedCheck: Boolean,
    ): UserProfileModel {
        return UserProfileModel(
            inputNickName,
            allowedCheck,
            inputNickName,
            null,
        )
    }

    private fun navigateToMainAcitivity(userProfile: UserProfileModel) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(SIGN_UP_AGREE, userProfile)
        startActivity(intent)
        finish()
    }

    private fun initBackBtnClickListener(flag: String) {
        when (flag) {
            MY_PAGE_PROFILE -> {
                binding.appbarSignUp.btnAppbarBack.setOnClickListener {
                    // 이전 프레그먼트로 돌아가는 코드
                    onBackPressedDispatcher.onBackPressed()
                    supportFragmentManager.popBackStack()
                }
            }

            SIGN_UP_AGREE -> {
                binding.appbarSignUp.btnAppbarBack.setOnClickListener {
                    finish()
                }
            }
        }
    }

    private fun initKeyboardSetting() {
        hideKeyboardOnClickBackground()
        requestFocusIntroduceEditText()
    }

    private fun hideKeyboardOnClickBackground() {
        binding.clSignUpProfileRoot.setOnClickListener {
            hideKeyboard(binding.root)
        }
    }

    private fun requestFocusIntroduceEditText() = with(binding) {
        etSignUpAgreeIntroduce.setOnClickListener {
            openKeyboard(etSignUpAgreeIntroduce)
        }
    }
}
