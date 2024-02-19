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
                is UiState.Success -> updateErrorMessage(it.data)
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun updateErrorMessage(doubleCheck: Boolean) {
        val messageResId =
            if (doubleCheck) R.string.sign_up_profile_use_posssible else R.string.sign_up_profile_use_impossible
        val textColorResId = if (doubleCheck) R.color.primary else R.color.error

        updateAgreeText(messageResId, textColorResId)
    }

    private fun initNextBtnStateObserve(flag: String) {
        binding.btnSignUpAgreeNext.setOnClickListener {
            handleSelectedButton(flag)
        }
    }

    private fun handleSelectedButton(flag: String) {
        val nickName = viewModel.nickName.value.orEmpty()
        val optionalAgreementInSignUp = intent.getBooleanExtra(SIGN_UP_AGREE, false)
        val introduceText = viewModel.introduceText.value.orEmpty()
        val imgUrl = null

        viewModel.saveUserNickNameInLocal(nickName)

        when (flag) {
            SIGN_UP_AGREE -> handleSignUpAgree(
                nickName = nickName,
                optionalAgreement = optionalAgreementInSignUp,
                introduce = introduceText,
                imgUrl = imgUrl
            )
            // 마이페이지 인 경우 선택 동의 null
            MY_PAGE_PROFILE -> handleMyPageProfile(
                nickName = nickName,
                introduce = introduceText,
                imgUrl = imgUrl
            )
        }
    }

    private fun handleSignUpAgree(
        nickName: String,
        optionalAgreement: Boolean,
        introduce: String,
        imgUrl: String?
    ) {
        viewModel.patchUserProfileEdit(nickName, optionalAgreement, introduce, imgUrl)
        navigateToMainActivity(setUpUserProfile(nickName, optionalAgreement, introduce, imgUrl))
    }

    private fun handleMyPageProfile(nickName: String, introduce: String, imgUrl: String?) {
        viewModel.patchUserProfileEdit(nickName, null, introduce, imgUrl)
        finish()
    }

    private fun navigateToMainActivity(userProfile: UserProfileModel) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(SIGN_UP_AGREE, userProfile)
        startActivity(intent)
        finish()
    }

    private fun setUpUserProfile(
        inputNickName: String,
        allowedCheck: Boolean,
        introduce: String,
        imgUrl: String?
    ): UserProfileModel {
        return UserProfileModel(
            inputNickName,
            allowedCheck,
            introduce,
            imgUrl,
        )
    }

    private fun initBackBtnClickListener(flag: String) {
        when (flag) {
            SIGN_UP_AGREE -> {
                binding.appbarSignUp.btnAppbarBack.setOnClickListener {
                    finish()
                }
            }

            MY_PAGE_PROFILE -> {
                binding.appbarSignUp.btnAppbarBack.setOnClickListener {
                    // 이전 프레그먼트로 돌아가는 코드
                    onBackPressedDispatcher.onBackPressed()
                    supportFragmentManager.popBackStack()
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
