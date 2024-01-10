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
            val nickName = binding.etSignUpAgreeText.text.toString()
            viewModel.validateNickName(nickName)
        }
    }

    private fun initNickNameExistObserving() {
        viewModel.isNickNameExist.observe(this) {
            if (it) {
                binding.tvSignUpAgreeMessage.apply {
                    text = context.getString(R.string.sign_up_profile_use_posssible)
                    setTextColor(colorOf(R.color.secondary))
                }
            } else {
                binding.tvSignUpAgreeMessage.apply {
                    text = context.getString(R.string.sign_up_profile_use_impossible)
                    setTextColor(colorOf(R.color.error))
                }
            }
        }
    }
}
