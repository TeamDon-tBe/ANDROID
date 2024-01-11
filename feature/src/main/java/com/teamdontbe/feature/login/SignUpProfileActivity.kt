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
        initNickNameExistObserving()
    }

    private fun initBtnSignUpProfileClickListner() {
        binding.btnSignUpProfileDoubleCheck.setOnClickListener {
            viewModel.validateNickName(binding.etSignUpAgreeText.text.toString())
        }
    }

    private fun initNickNameExistObserving() {
        viewModel.isNickNameExist.observe(this) {
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
