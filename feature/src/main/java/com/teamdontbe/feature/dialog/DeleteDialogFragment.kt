package com.teamdontbe.feature.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteDialogBinding

class DeleteDialogFragment(private val content: String) :
    BindingDialogFragment<FragmentDeleteDialogBinding>(R.layout.fragment_delete_dialog) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        initContentText()
        initCancelButtonClick()
        initDeleteButtonClick()
    }

    private fun initContentText() {
        binding.tvDeleteDialogContent.text = content
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun initCancelButtonClick() {
        binding.btnDeleteDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initDeleteButtonClick() {
        binding.btnDeleteDialogDelete.setOnClickListener {
            navigateToPreviousActivity()
            // navigateToMainActivity()
            dismiss()
        }
    }

    private fun navigateToMainActivity() {
        findNavController().navigate(
            R.id.action_posting_to_home,
        )
    }

    private fun navigateToPreviousActivity() {
        findNavController().navigateUp()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
