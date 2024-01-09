package com.teamdontbe.feature.posting

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentPostingBinding
import com.teamdontbe.feature.util.Debouncer

class PostingFragment : BindingFragment<FragmentPostingBinding>(R.layout.fragment_posting) {
    private val postingDebouncer = Debouncer<String>()

    override fun initView() {
        initEditText()
        initCancelBtnClickListener()
    }

    private fun initCancelBtnClickListener() {
        // 다이얼로그 추가 후 코드 변경 필요
        binding.appbarPosting.tvAppbarCancel.setOnClickListener {
            navigateToPreviousActivity()
        }
    }

    private fun navigateToPreviousActivity() {
        findNavController().navigateUp()
    }

    private fun initUploadingDeactivateBtnClickListener() {
        binding.btnPostingUpload.setOnClickListener {
            null
        }
    }

    private fun initUploadingActivateBtnClickListener() {
        binding.btnPostingUpload.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        findNavController().navigate(
            R.id.action_posting_to_home
        )
    }

    private fun initEditText() {
        binding.run {
            etPostingContent.doAfterTextChanged {
                val inputString = etPostingContent.text.toString()
                val inputStringLen = inputString.length

                when {
                    inputStringLen in 1..499 -> {
                        pbPostingInput.progressDrawable = context?.let { it ->
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.shape_primary_line_10_ring
                            )
                        }
                        pbPostingInput.progress = inputString.length
                        btnPostingUpload.setImageResource(R.drawable.ic_posting_uploading_activate)
                        initUploadingActivateBtnClickListener()
                    }

                    inputStringLen >= 500 -> {
                        pbPostingInput.progressDrawable = context?.let { it ->
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.shape_error_line_10_ring
                            )
                        }
                        pbPostingInput.progress = inputString.length
                        btnPostingUpload.setImageResource(R.drawable.ic_posting_uploading_deactivate)
                        initUploadingDeactivateBtnClickListener()
                    }

                    else -> {
                        pbPostingInput.progress = 0
                        btnPostingUpload.setImageResource(R.drawable.ic_posting_uploading_deactivate)
                        initUploadingDeactivateBtnClickListener()
                    }
                }
                postingDebouncer.setDelay(inputString, 1000L) {}
            }
        }
    }
}
