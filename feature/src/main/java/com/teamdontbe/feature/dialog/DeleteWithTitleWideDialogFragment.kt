package com.teamdontbe.feature.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.core_ui.util.intent.navigateTo
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleWideDialogBinding
import com.teamdontbe.feature.login.LoginActivity
import com.teamdontbe.feature.mypage.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteWithTitleWideDialogFragment(
    private val title: String, private val content: String, private val typeIsLogout: Boolean
) : BindingDialogFragment<FragmentDeleteWithTitleWideDialogBinding>(R.layout.fragment_delete_with_title_wide_dialog) {

    private val viewModel by viewModels<MyPageViewModel>()

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
                true -> {
                    viewModel.checkLogin(false)
                    navigateToLoginActivity()
                    dismiss()
                }
                false -> {
                    //계정 삭제 로직 적기
                }
            }

        }
    }

    private fun navigateToLoginActivity() {
        // 추후 활성화해야 함
        navigateTo<LoginActivity>(requireActivity())
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
