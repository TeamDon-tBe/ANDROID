package com.teamdontbe.feature.homedetail

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.core_ui.util.fragment.colorOf
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.core_ui.view.setOnDuplicateBlockClick
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetCommentBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.posting.AnimateProgressBar
import com.teamdontbe.feature.util.Debouncer
import com.teamdontbe.feature.util.DialogTag.DELETE_COMMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentBottomSheet(
    val contentId: Int,
    val feed: FeedEntity
) : BindingBottomSheetFragment<BottomsheetCommentBinding>(R.layout.bottomsheet_comment),
    DeleteDialogListener {
    private val commentDebouncer = Debouncer<String>()
    private val homeViewModel by activityViewModels<HomeViewModel>()
    override fun initView() {
        binding.vm = homeViewModel
        binding.feed = feed
        setShowKeyboard()
        initEditText()
        initAppbarCancelClickListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener { dialogInterface ->
                val bottomSheet =
                    (dialogInterface as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let { sheet ->
                    sheet.layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                }
            }
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isDraggable = false
        }
        return dialog
    }

    private fun setShowKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            binding.etCommentContent,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun initEditText() {
        binding.etCommentContent.doAfterTextChanged { text ->
            val textLength = text?.length ?: 0
            val progressBarDrawableId = getProgressBarDrawableId(textLength)
            val btnBackgroundTint = getButtonBackgroundTint(textLength)
            val btnTextColor = getButtonTextColor(textLength)

            updateUploadBar(textLength, progressBarDrawableId, btnBackgroundTint, btnTextColor)
            initUploadingBtnClickListener(textLength)
            startProgressBarAnimation(textLength)
            debounceComment(text.toString())
        }
    }

    private fun getProgressBarDrawableId(textLength: Int): Int =
        if (textLength >= HomeDetailFragment.MAX_COMMENT_LENGTH) R.drawable.shape_error_line_circle else R.drawable.shape_primary_line_circle

    private fun getButtonBackgroundTint(textLength: Int): ColorStateList =
        if (textLength in HomeDetailFragment.MIN_COMMENT_LENGTH until HomeDetailFragment.MAX_COMMENT_LENGTH) {
            ColorStateList.valueOf(
                colorOf(R.color.primary),
            )
        } else {
            ColorStateList.valueOf(colorOf(R.color.gray_3))
        }

    private fun getButtonTextColor(textLength: Int): Int =
        if (textLength in HomeDetailFragment.MIN_COMMENT_LENGTH until HomeDetailFragment.MAX_COMMENT_LENGTH) {
            colorOf(R.color.black)
        } else {
            colorOf(
                R.color.gray_9,
            )
        }

    private fun updateUploadBar(
        textLength: Int,
        progressBarDrawableId: Int,
        btnBackgroundTint: ColorStateList,
        btnTextColor: Int,
    ) {
        with(binding.layoutUploadBar) {
            pbUploadBarInput.progressDrawable = drawableOf(progressBarDrawableId)
            pbUploadBarInput.progress = textLength
            btnUploadBarUpload.backgroundTintList = btnBackgroundTint
            btnUploadBarUpload.setTextColor(btnTextColor)
        }
    }

    private fun startProgressBarAnimation(textLength: Int) {
        val uploadBar = binding.layoutUploadBar
        val animateProgressBar = AnimateProgressBar(
            uploadBar.pbUploadBarInput,
            0f,
            textLength.toFloat(),
        )
        uploadBar.pbUploadBarInput.startAnimation(animateProgressBar)
    }

    private fun debounceComment(commentText: String) {
        commentDebouncer.setDelay(commentText, HomeDetailFragment.COMMENT_DEBOUNCE_DELAY) {}
    }

    private fun initUploadingBtnClickListener(textLength: Int) {
        binding.layoutUploadBar.btnUploadBarUpload.setOnDuplicateBlockClick {
            if (textLength in HomeDetailFragment.MIN_COMMENT_LENGTH until HomeDetailFragment.MAX_COMMENT_LENGTH) {
                homeViewModel.postCommentPosting(
                    contentId,
                    binding.etCommentContent.text.toString(),
                )
                dismiss()
            }
        }
    }

    private fun initAppbarCancelClickListener() {
        binding.tvCommentAppbarCancel.setOnClickListener {
            if (binding.etCommentContent.text.isEmpty()) {
                dismiss()
            } else {
                val dialog = DeleteDialogFragment(getString(R.string.comment_delete_dialog))
                dialog.setDeleteDialogListener(this)
                dialog.show(childFragmentManager, DELETE_COMMENT)
            }
        }
    }

    override fun onDeleteDialogDismissed() {
        dismiss()
    }
}
