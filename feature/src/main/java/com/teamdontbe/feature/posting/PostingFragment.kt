package com.teamdontbe.feature.posting

import android.view.animation.AnimationUtils
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.drawableOf
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentPostingBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.util.Debouncer

class PostingFragment : BindingFragment<FragmentPostingBinding>(R.layout.fragment_posting) {
    private val postingDebouncer = Debouncer<String>()

    override fun initView() {
        statusBarColorOf(R.color.white)

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_posting_fragment_from_right)
        view?.startAnimation(animation)

        initEditText()
        initCancelBtnClickListener()
    }

    private fun initCancelBtnClickListener() {
        binding.appbarPosting.tvAppbarCancel.setOnClickListener {
            val dialog = DeleteDialogFragment(getString(R.string.posting_delete_dialog))
            dialog.show(childFragmentManager, DELETE_POSTING)
        }
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
            R.id.action_posting_to_home,
        )
    }

    private fun initEditText() {
        binding.run {
            etPostingContent.doAfterTextChanged {
                when {
                    etPostingContent.text.toString().length in 1..499 -> {
                        pbPostingInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_primary_line_10_ring)
                        pbPostingInput.progress = etPostingContent.text.toString().length
                        btnPostingUpload.setImageResource(R.drawable.ic_posting_uploading_activate)
                        initUploadingActivateBtnClickListener()
                    }

                    etPostingContent.text.toString().length >= 500 -> {
                        pbPostingInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_error_line_10_ring)
                        pbPostingInput.progress = etPostingContent.text.toString().length
                        btnPostingUpload.setImageResource(R.drawable.ic_posting_uploading_deactivate)
                        initUploadingDeactivateBtnClickListener()
                    }

                    else -> {
                        pbPostingInput.progress = 0
                        btnPostingUpload.setImageResource(R.drawable.ic_posting_uploading_deactivate)
                        initUploadingDeactivateBtnClickListener()
                    }
                }
                postingDebouncer.setDelay(etPostingContent.text.toString(), 1000L) {}
            }
        }
    }

    companion object {
        const val DELETE_POSTING = "delete_posting"
    }
}
