package com.teamdontbe.feature.login

import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.colorOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivitySignUpProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpProfileActivity :
    BindingActivity<ActivitySignUpProfileBinding>(R.layout.activity_sign_up_profile) {

    private val viewModel by viewModels<SignUpProfileViewModel>()

    override fun initView() {
        binding.vm = viewModel
        initBtnSignUpProfileClickListner()
//        initNickNameExistObserving()
        initUpdateErrorMessage()
    }

    private fun initBtnSignUpProfileClickListner() {
        binding.btnSignUpProfileDoubleCheck.setOnClickListener {
            viewModel.updateNickNameBtnValidity(binding.etSignUpProfileNickname.text.toString())
        }
    }

    private fun initUpdateErrorMessage() {
        viewModel.isNickNameValid.observe(this) {
//            val textColorResId = if (it) R.color.secondary else R.color.error

            if (it) {
                binding.tvSignUpAgreeMessage.text = "사용 가능한 닉네임 입니다"
            } else {
                binding.tvSignUpAgreeMessage.text = "공백 없이 한글 영어 포함 12자 이내로 작성해 주세요"
            }
        }
    }

    private fun initNickNameExistObserving() {
        viewModel.isBtnSelected.observe(this) {
            val messageResId =
                if (it) R.string.sign_up_profile_use_posssible else R.string.sign_up_profile_use_impossible
            val textColorResId = if (it) R.color.secondary else R.color.error

            binding.tvSignUpAgreeMessage.apply {
                text = context.getString(messageResId)
                setTextColor(colorOf(textColorResId))
            }
        }
    }
}
