package com.teamdontbe.feature.dialog

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.kakao.sdk.user.UserApiClient
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.AmplitudeUtil.trackEvent
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.core_ui.util.fragment.viewLifeCycle
import com.teamdontbe.core_ui.util.fragment.viewLifeCycleScope
import com.teamdontbe.core_ui.util.intent.navigateTo
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.ErrorActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleWideDialogBinding
import com.teamdontbe.feature.login.LoginActivity
import com.teamdontbe.feature.mypage.authwithdraw.MyPageAuthWithdrawViewModel
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_ACCOUNT_DELETE_DONE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class DeleteWithTitleWideDialogFragment(
    private val title: String,
    private val content: String,
    private val typeIsLogout: Boolean,
    private val selectedReason: String,
) : BindingDialogFragment<FragmentDeleteWithTitleWideDialogBinding>(R.layout.fragment_delete_with_title_wide_dialog) {
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
        deleteObserve()
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

    private fun deleteObserve() {
        withdrawViewModel.patchWithdraw.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    Timber.tag("withdraw").i("계정 삭제 성공")
                    withdrawViewModel.clearInfo()
                    navigateToLoginActivity()
                    dismiss()
                }
                is UiState.Failure -> startActivity(Intent(context, ErrorActivity::class.java))
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initDeleteButtonClick() {
        binding.btnDeleteWithTitleWideDialogDelete.setOnClickListener {
            when (typeIsLogout) {
                true -> {
                    withdrawViewModel.checkLogin(false)
                    navigateToLoginActivity()
                    dismiss()
                }
                false -> {
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Timber.e("연결 끊기 실패", error)
                        } else {
                            trackEvent(CLICK_ACCOUNT_DELETE_DONE)
                            Timber.i("연결 끊기 성공. SDK에서 토큰 삭제 됨")
                            withdrawViewModel.patchWithdraw(selectedReason)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToLoginActivity() {
        navigateTo<LoginActivity>(requireActivity())
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
