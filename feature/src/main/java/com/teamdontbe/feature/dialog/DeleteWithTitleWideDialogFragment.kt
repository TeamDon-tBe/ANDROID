package com.teamdontbe.feature.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.user.UserApiClient
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.core_ui.util.intent.navigateTo
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleWideDialogBinding
import com.teamdontbe.feature.login.LoginActivity
import com.teamdontbe.feature.mypage.MyPageViewModel
import com.teamdontbe.feature.mypage.authwithdraw.MyPageAuthWithdrawViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class DeleteWithTitleWideDialogFragment(
    private val title: String,
    private val content: String,
    private val typeIsLogout: Boolean,
    private val selectedReason: String
) : BindingDialogFragment<FragmentDeleteWithTitleWideDialogBinding>(R.layout.fragment_delete_with_title_wide_dialog) {
    private val myPageViewModel by viewModels<MyPageViewModel>()
    private val withdrawViewModel by viewModels<MyPageAuthWithdrawViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        initText()
        initCancelButtonClick()
        initDeleteButtonClick()
    }

    private fun initText() {
        binding.tvDeleteWithTitleWideDialogTitle.text = title
        binding.tvDeleteWithTitleWideDialogContent.text = content
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun initCancelButtonClick() {
        binding.btnDeleteWithTitleWideDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initDeleteButtonClick() {
        binding.btnDeleteWithTitleWideDialogDelete.setOnClickListener {
            when (typeIsLogout) {
                true -> myPageViewModel.checkLogin(false)
                false -> {
                    // 계정 삭제 로직 적기
                    withdrawViewModel.deleteWithdraw.flowWithLifecycle(lifecycle).onEach {
                        when (it) {
                            is UiState.Loading -> Unit
                            is UiState.Success -> {
                                // 여기 추가하기
                                UserApiClient.instance.unlink { error ->
                                    if (error != null) {
                                        Timber.e("연결 끊기 실패", error)
                                    }
                                    else {
                                        Timber.i("연결 끊기 성공. SDK에서 토큰 삭제 됨")
                                    }
                                }
                                withdrawViewModel.deleteWithdraw(selectedReason)
                            }
                            else -> Unit
                        }
                    }.launchIn(lifecycleScope)
                }
            }
            navigateToLoginActivity()
            dismiss()
        }
    }

    private fun navigateToLoginActivity() {
        navigateTo<LoginActivity>(requireActivity())
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
