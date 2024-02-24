package com.teamdontbe.feature.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.core_ui.util.intent.navigateTo
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleWideDialogBinding
import com.teamdontbe.feature.login.LoginActivity
import com.teamdontbe.feature.mypage.authwithdraw.MyPageAuthWithdrawViewModel
import dagger.hilt.android.AndroidEntryPoint
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
                true -> withdrawViewModel.checkLogin(false)
                false -> {
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Timber.e("연결 끊기 실패", error)
                        } else {
                            Timber.i("연결 끊기 성공. SDK에서 토큰 삭제 됨")
                            withdrawViewModel.deleteWithdraw(selectedReason)
                        }
                    }
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
