package com.teamdontbe.feature.login

import android.content.Intent
import android.view.View
import android.widget.CheckBox
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivitySignUpAgreeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpAgreeActivity :
    BindingActivity<ActivitySignUpAgreeBinding>(R.layout.activity_sign_up_agree) {

    private val childCheckBoxList by lazy {
        with(binding) {
            listOf(
                cbSignUpChild1,
                cbSignUpChild2,
                cbSignUpChild3,
                cbSignUpChild4,
            )
        }
    }

    override fun initView() {
        binding.appbarSignUp.btnAppbarBack.visibility = View.INVISIBLE
        initParentCheckBoxSignUpAgreeListener()
        initChildCheckBoxSignUpAgreeListener()
    }

    private fun initParentCheckBoxSignUpAgreeListener() = with(binding) {
        cbSignUpParent.setOnCheckedChangeListener { _, isChecked ->
            childCheckBoxList.forEach { it.isChecked = isChecked }
        }
    }

    private fun initChildCheckBoxSignUpAgreeListener() {
        childCheckBoxList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                updateNextBtnState(childCheckBoxList)
            }
        }
    }

    private fun updateNextBtnState(childCheckBoxList: List<CheckBox>) {
        val allChecked = childCheckBoxList.all { it.isChecked }
        binding.btnSignUpAgreeNext.apply {
            isEnabled = allChecked
            if (isEnabled) {
                setOnClickListener {
                    navigateUpToSignUpProfileActivity()
                }
            }
        }
    }

    private fun navigateUpToSignUpProfileActivity() {
        startActivity(Intent(this, SignUpProfileActivity::class.java))
    }
}
