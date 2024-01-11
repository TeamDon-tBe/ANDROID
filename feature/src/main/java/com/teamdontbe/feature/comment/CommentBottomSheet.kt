package com.teamdontbe.feature.comment

import androidx.core.widget.doAfterTextChanged
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.core_ui.util.context.drawableOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetCommentBinding
import com.teamdontbe.feature.util.Debouncer

class CommentBottomSheet : BindingBottomSheetFragment<BottomsheetCommentBinding>(R.layout.bottomsheet_complaint_delete) {
    private val commentDebouncer = Debouncer<String>()

    override fun initView() {
    }

    private fun initEditText() {
        binding.run {
            etCommentContent.doAfterTextChanged {
                when {
                    etCommentContent.text.toString().length in 1..499 -> {
                        layoutUploadBar.pbUploadBarInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_primary_line_10_ring)
                        layoutUploadBar.pbUploadBarInput.progress = etCommentContent.text.toString().length
                        layoutUploadBar.btnUploadBarUpload.setImageResource(R.drawable.ic_uploading_activate)
                        initUploadingBtnClickListener()
                    }

                    etCommentContent.text.toString().length >= 500 -> {
                        layoutUploadBar.pbUploadBarInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_error_line_10_ring)
                        layoutUploadBar.pbUploadBarInput.progress = etCommentContent.text.toString().length
                        layoutUploadBar.btnUploadBarUpload.setImageResource(R.drawable.ic_uploading_deactivate)
                    }

                    else -> {
                        layoutUploadBar.pbUploadBarInput.progress = 0
                        layoutUploadBar.btnUploadBarUpload.setImageResource(R.drawable.ic_uploading_deactivate)
                    }
                }
                commentDebouncer.setDelay(binding.etCommentContent.text.toString(), 1000L) {
                }
            }
        }
    }

    private fun initUploadingBtnClickListener() {
        binding.layoutUploadBar.btnUploadBarUpload.setOnClickListener {
        }
    }
}
