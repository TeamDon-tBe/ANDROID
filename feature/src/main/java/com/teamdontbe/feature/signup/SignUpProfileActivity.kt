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
        initUpdateErrorMessage()
        initDoubleBtnClickListener(flag)
        initMyPageStateObserve()
        initBackBtnClickListener(flag)
        hideKeyboard()
        requestFocusIntroduceEditText()
    }

    private fun hideKeyboard() {
        binding.clSignUpProfileRoot.setOnClickListener {
            hideKeyboard(binding.root)
        }
    }

    private fun requestFocusIntroduceEditText() =
        with(binding) {
            etSignUpAgreeIntroduce.setOnClickListener {
                openKeyboard(etSignUpAgreeIntroduce)
            }
        }

    private fun initMyPageProfileAppBarTitle(): String {
        when {
            intent.getStringExtra(MY_PAGE_PROFILE) != null -> {
                val myPageAppBarTitle =
                    intent.getStringExtra(MY_PAGE_PROFILE) ?: getString(R.string.my_page_nickname)
                binding.appbarSignUp.tvAppbarTitle.text = myPageAppBarTitle
                binding.btnSignUpAgreeNext.text = getString(R.string.my_page_profile_edit_completed)

                return MY_PAGE_PROFILE
            }

            intent.hasExtra(SIGN_UP_AGREE) -> {
                binding.groupSignUpIntroduce.visibility = View.INVISIBLE
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                
                return SIGN_UP_AGREE
            }

            else -> return SIGN_UP_AGREE
        }
    }

    private fun initUpdateErrorMessage() {
        viewModel.isNickNameValid.observe(this) {
            val messageResId =
                if (it) R.string.sign_up_profile_check_text else R.string.sign_up_profile_correct_check
            val textColorResId = if (it) R.color.gray_8 else R.color.error

            updateAgreeMessage(messageResId, textColorResId)
        }
    }

    private fun initDoubleBtnClickListener(flag: String) {
        binding.btnSignUpProfileDoubleCheck.setOnClickListener {
            val inputNickName = binding.etSignUpProfileNickname.text.toString()
            viewModel.getNickNameDoubleCheck(inputNickName)
            nextBtnObserve(inputNickName, flag)
            when (flag) {
                MY_PAGE_PROFILE -> binding.etSignUpAgreeIntroduce.requestFocus()
                SIGN_UP_AGREE -> hideKeyboard(binding.root)
            }
        }
    }

    private fun updateErrorMessage(doubleCheck: Boolean) {
        val messageResId =
            if (doubleCheck) R.string.sign_up_profile_use_posssible else R.string.sign_up_profile_use_impossible
        val textColorResId = if (doubleCheck) R.color.primary else R.color.error

        updateAgreeMessage(messageResId, textColorResId)
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

    private fun updateAgreeMessage(
        messageResId: Int,
        textColorResId: Int,
    ) {
        binding.tvSignUpAgreeMessage.apply {
            text = context.getString(messageResId)
            setTextColor(colorOf(textColorResId))
        }
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
}
