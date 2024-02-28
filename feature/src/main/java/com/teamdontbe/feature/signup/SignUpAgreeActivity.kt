package com.teamdontbe.feature.signup

import android.content.Intent
import android.net.Uri
import android.widget.CheckBox
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivitySignUpAgreeBinding
import com.teamdontbe.feature.login.LoginActivity
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
        initAppBarTextSetting()
        initBackBtnClickListener()
        initCheckBoxClickListener()
        initInfoUriBtnClickListener()
    }

    private fun initAppBarTextSetting() {
        binding.appbarSignUp.tvAppbarTitle.text = getString(R.string.sign_up_appbar_title)
    }

    private fun initBackBtnClickListener() {
        binding.appbarSignUp.btnAppbarBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun initCheckBoxClickListener() {
        initParentCheckBoxSignUpAgreeClickListener()
        initChildCheckBoxSignUpAgreeListener()
    }

    private fun initParentCheckBoxSignUpAgreeClickListener() {
        binding.cbSignUpParent.setOnCheckedChangeListener { buttonView, isChecked ->
            // 사용자가 눌렀을 때만 해당 child check 로직 실행
            if (buttonView.isPressed) {
                childCheckBoxList.forEach { it.isChecked = isChecked }
                updateNextBtnState(childCheckBoxList)
            }
        }
    }

    private fun initChildCheckBoxSignUpAgreeListener() {
        childCheckBoxList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                binding.cbSignUpParent.isChecked = childCheckBoxList.all { it.isChecked }
                updateNextBtnState(childCheckBoxList)
            }
        }
    }

    private fun updateNextBtnState(childCheckBoxList: List<CheckBox>) {
        val allChecked = childCheckBoxList.take(3).all { it.isChecked }
        binding.btnSignUpAgreeNext.apply {
            isEnabled = allChecked
            if (isEnabled) {
                setTextColor(getColor(R.color.white))
                setOnClickListener {
                    navigateUpToSignUpProfileActivity()
                }
            } else {
                setTextColor(getColor(R.color.gray_9))
            }
        }
    }

    private fun navigateUpToSignUpProfileActivity() {
        val intent = Intent(this, SignUpProfileActivity::class.java)
        val isChild4Checked = binding.cbSignUpChild4.isChecked
        intent.putExtra(SIGN_UP_AGREE, isChild4Checked)
        startActivity(intent)
    }

    private fun initInfoUriBtnClickListener() {
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

    companion object {
        const val SIGN_UP_AGREE = "signUpAgree"
    }
}
