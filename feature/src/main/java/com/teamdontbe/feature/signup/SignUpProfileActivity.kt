package com.teamdontbe.feature.signup

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.colorOf
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
    }

    private fun initMyPageProfileAppBarTitle(): Int {
        return when {
            intent.getStringExtra(MY_PAGE_PROFILE) != null -> {
                val myPageAppBarTitle =
                    intent.getStringExtra(MY_PAGE_PROFILE) ?: getString(R.string.my_page_nickname)
                binding.appbarSignUp.tvAppbarTitle.text = myPageAppBarTitle
                binding.btnSignUpAgreeNext.text = getString(R.string.my_page_profile_edit_completed)
                0
            }

            intent.hasExtra(SIGN_UP_AGREE) -> {
                binding.etSignUpAgreeIntroduce.visibility = View.INVISIBLE
                binding.tvSignUpProfile.visibility = View.INVISIBLE
                binding.tvSignUpProfileIntroduceNum.visibility = View.INVISIBLE
                1
            }

            else -> 1
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

    private fun initDoubleBtnClickListener(flag: Int) {
        binding.btnSignUpProfileDoubleCheck.setOnClickListener {
            val inputNickName = binding.etSignUpProfileNickname.text.toString()
            viewModel.getNickNameDoubleCheck(inputNickName)
            nextBtnObserve(inputNickName, flag)
        }
    }

    private fun updateErrorMessage(doubleCheck: Boolean) {
        val messageResId =
            if (doubleCheck) R.string.sign_up_profile_use_posssible else R.string.sign_up_profile_use_impossible
        val textColorResId = if (doubleCheck) R.color.primary else R.color.error

        updateAgreeMessage(messageResId, textColorResId)
    }

    private fun initMyPageStateObserve() {
        viewModel.nickNameDoubleState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    if (it.data.isEmpty()) {
                        updateErrorMessage(false)
                    } else {
                        updateErrorMessage(true)
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun updateAgreeMessage(messageResId: Int, textColorResId: Int) {
        binding.tvSignUpAgreeMessage.apply {
            text = context.getString(messageResId)
            setTextColor(colorOf(textColorResId))
        }
    }

    private fun nextBtnObserve(inputNickName: String, flag: Int) {
        binding.btnSignUpAgreeNext.setOnClickListener {
            viewModel.isBtnSelected.observe(this) {
                if (it) {
                    viewModel.patchUserProfileEdit(
                        inputNickName,
                        intent.getBooleanExtra(SIGN_UP_AGREE, false),
                        binding.etSignUpAgreeIntroduce.text.toString(),
                        "",
                    )
                }
            }
            when (flag) {
                0 -> finish()

                1 -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
