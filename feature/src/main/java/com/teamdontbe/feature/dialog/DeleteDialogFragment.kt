package com.teamdontbe.feature.dialog

import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteDialogBinding
import com.teamdontbe.feature.homedetail.DeleteDialogListener
import com.teamdontbe.feature.util.DialogTag.DELETE_COMMENT
import com.teamdontbe.feature.util.KeyStorage

class DeleteDialogFragment(private val content: String) :
    BindingDialogFragment<FragmentDeleteDialogBinding>(R.layout.fragment_delete_dialog) {
    private var deleteDialogListener: DeleteDialogListener? = null

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
            when (tag) {
                KeyStorage.DELETE_POSTING -> navigateToPreviousActivity()
                DELETE_COMMENT -> deleteDialogListener?.onDeleteDialogDismissed()
            }
            dismiss()
        }
    }

    private fun navigateToPreviousActivity() {
        findNavController().navigateUp()
    }

    fun setDeleteDialogListener(listener: DeleteDialogListener) {
        deleteDialogListener = listener
    }
}
