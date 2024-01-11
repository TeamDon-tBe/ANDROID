package com.teamdontbe.feature.comment

import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.core_ui.util.context.drawableOf
import com.teamdontbe.core_ui.util.context.openKeyboard
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetCommentBinding
import com.teamdontbe.feature.util.Debouncer

class CommentBottomSheet :
    BindingBottomSheetFragment<BottomsheetCommentBinding>(R.layout.bottomsheet_comment) {
    private val commentDebouncer = Debouncer<String>()

    override fun initView() {
        initEditText()
        requireContext().openKeyboard(View(requireContext()))
    }

    private fun initEditText() {
        binding.run {
            etCommentContent.doAfterTextChanged {
                when {
                    etCommentContent.text.toString().length in POSSIBLE_LENGTH -> {
                        updateUploadBar(
                            R.drawable.shape_primary_line_10_ring,
                            etCommentContent.text.toString().length,
                            R.drawable.ic_uploading_activate,
                        )
                        initUploadingBtnClickListener()
                    }

                    etCommentContent.text.toString().length >= MAX_LENGTH -> {
                        updateUploadBar(
                            R.drawable.shape_error_line_10_ring,
                            etCommentContent.text.toString().length,
                            R.drawable.ic_uploading_deactivate,
                        )
                    }

                    else -> {
                        layoutUploadBar.pbUploadBarInput.progress = 0
                        layoutUploadBar.btnUploadBarUpload.setImageResource(R.drawable.ic_uploading_deactivate)
                    }
                }
                commentDebouncer.setDelay(binding.etCommentContent.text.toString(), DELAY) {}
            }
        }
    }

    private fun updateUploadBar(
        progressDrawableResId: Int,
        progress: Int,
        imageResourceResId: Int,
    ) {
        with(binding) {
            layoutUploadBar.pbUploadBarInput.progressDrawable =
                context?.drawableOf(progressDrawableResId)
            layoutUploadBar.pbUploadBarInput.progress = progress
            layoutUploadBar.btnUploadBarUpload.setImageResource(imageResourceResId)
        }
    }

    private fun initUploadingBtnClickListener() {
        binding.layoutUploadBar.btnUploadBarUpload.setOnClickListener {}
    }

    companion object {
        const val DELAY = 1000L
        val POSSIBLE_LENGTH = 1..499
        const val MAX_LENGTH = 500
    }
}
