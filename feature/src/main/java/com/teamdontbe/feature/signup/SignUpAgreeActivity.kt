package com.teamdontbe.feature.signup

import android.content.Intent
import android.net.Uri
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
        initParentCheckBoxSignUpAgreeClickListener()
        initChildCheckBoxSignUpAgreeListener()
        initBtnClickListener()
    }

    private fun initBtnClickListener() {
        binding.btnSignUpAgreeChild1.setOnClickListener {
            navigateToComplaintWeb("https://www.notion.so/93625ba2f93547ff88984d3bb82a2f32")
        }
        binding.btnSignUpAgreeChild2.setOnClickListener {
            navigateToComplaintWeb("https://www.notion.so/1681f9cae9de47858ee0997b4cea9c03")
        }
        binding.btnSignUpAgreeChild4.setOnClickListener {
            navigateToComplaintWeb("https://www.notion.so/0c70bf474acb487ab2b2ae957d975e51")
        }
    }

    private fun navigateToComplaintWeb(uri: String) {
        val urlIntentComplaint =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri),
            )
        startActivity(urlIntentComplaint)
    }

    private fun initParentCheckBoxSignUpAgreeClickListener() =
        with(binding) {
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
        val allChecked = childCheckBoxList.take(3).all { it.isChecked }
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
        val intent = Intent(this, SignUpProfileActivity::class.java)
        val isChild4Checked = binding.cbSignUpChild4.isChecked
        intent.putExtra(SIGN_UP_AGREE, isChild4Checked)
        startActivity(intent)
        finish()
    }

    companion object {
        const val SIGN_UP_AGREE = "signUpAgree"
    }
}
