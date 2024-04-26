package com.teamdontbe.feature.homedetail

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.core_ui.util.AmplitudeUtil.trackEvent
import com.teamdontbe.core_ui.util.fragment.colorOf
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.core_ui.view.setOnDuplicateBlockClick
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetCommentBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.homedetail.HomeDetailFragment.Companion.COMMENT_DEBOUNCE_DELAY
import com.teamdontbe.feature.posting.AnimateProgressBar
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_DEBOUNCE_DELAY
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_MAX
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_MAX_WITH_LINK
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_MIN
import com.teamdontbe.feature.posting.PostingFragment.Companion.WEB_URL_PATTERN
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_REPLY_UPLOAD
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
    private var totalCommentLength = 0
    private var linkValidity = true

    override fun initView() {
        binding.vm = homeViewModel
        binding.feed = feed
        setShowKeyboard()
        initEditText()
        initAppbarCancelClickListener()
        initLinkBtnClickListener()
        initCancelLinkBtnClickListener()
        checkLinkValidity()
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

    private fun initLinkBtnClickListener() = with(binding) {
        layoutUploadBar.ivUploadLink.setOnClickListener {
            if (etCommentLink.isVisible) setLinkErrorMessageValidity(
                linkValidity = WEB_URL_PATTERN.matcher(binding.etCommentLink.text.toString())
                    .find(),
                linkCountValidity = true
            )
            handleLinkAndCancelBtnVisible(true)
            etCommentLink.requestFocus()
        }
    }

    private fun initCancelLinkBtnClickListener() = with(binding) {
        ivCommentCancelLink.setOnClickListener {
            handleLinkAndCancelBtnVisible(false)
            etCommentLink.text.clear()
            etCommentContent.requestFocus()
            setLinkErrorMessageValidity(linkValidity = true, linkCountValidity = false)
            linkValidity = true
            setUploadingCommentState(totalCommentLength)
        }
    }

    private fun handleLinkAndCancelBtnVisible(isVisible: Boolean) = with(binding) {
        etCommentLink.isVisible = isVisible
        ivCommentCancelLink.isVisible = isVisible
    }

    private fun checkLinkValidity() = with(binding.etCommentLink) {
        doAfterTextChanged {
            binding.etCommentContent.filters =
                arrayOf(InputFilter.LengthFilter(POSTING_MAX - text.toString().length))
            totalCommentLength = (binding.etCommentContent.text.toString() + text.toString()).length
            handleLinkErrorMessage(
                WEB_URL_PATTERN.matcher(
                    text.toString()
                ).find()
            )
            setUploadingCommentState(totalCommentLength)
            commentDebouncer.setDelay(
                text.toString(),
                POSTING_DEBOUNCE_DELAY
            ) {}
        }
    }

    private fun handleLinkErrorMessage(linkValidity: Boolean) {
        this.linkValidity = linkValidity
        setLinkErrorMessageValidity(linkValidity, false)
    }

    private fun setLinkErrorMessageValidity(linkValidity: Boolean, linkCountValidity: Boolean) =
        with(binding) {
            tvCommentLinkError.isVisible = !linkValidity
            tvCommentLinkCountError.isVisible = linkCountValidity
        }

    private fun initEditText() = with(binding) {
        etCommentContent.doAfterTextChanged { text ->
            etCommentLink.filters =
                arrayOf(InputFilter.LengthFilter(POSTING_MAX - text.toString().length))
            totalCommentLength =
                (etCommentContent.text.toString() + etCommentLink.text.toString()).length
            setUploadingCommentState(totalCommentLength)
            debounceComment(text.toString())
        }
    }

    private fun setUploadingCommentState(totalCommentLength: Int) {
        val progressBarDrawableId = getProgressBarDrawableId(totalCommentLength)
        val btnBackgroundTint = getButtonBackgroundTint(totalCommentLength)
        val btnTextColor = getButtonTextColor(totalCommentLength)

        updateUploadBar(totalCommentLength, progressBarDrawableId, btnBackgroundTint, btnTextColor)
        initUploadingBtnClickListener(totalCommentLength, linkValidity)
        startProgressBarAnimation(totalCommentLength)
    }

    private fun getProgressBarDrawableId(textLength: Int): Int =
        if (textLength >= POSTING_MAX_WITH_LINK) R.drawable.shape_error_line_circle else R.drawable.shape_primary_line_circle

    private fun getButtonBackgroundTint(textLength: Int): ColorStateList =
        if (textLength in POSTING_MIN..POSTING_MAX_WITH_LINK && linkValidity) {
            ColorStateList.valueOf(
                colorOf(R.color.primary),
            )
        } else {
            ColorStateList.valueOf(colorOf(R.color.gray_3))
        }

    private fun getButtonTextColor(textLength: Int): Int =
        if (textLength in POSTING_MIN..POSTING_MAX_WITH_LINK && linkValidity) {
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
            setUploadBtnColor(btnBackgroundTint, btnTextColor)
        }
    }

    private fun setUploadBtnColor(
        btnBackgroundTint: ColorStateList,
        btnTextColor: Int,
    ) = with(binding.layoutUploadBar) {
        btnUploadBarUpload.backgroundTintList = btnBackgroundTint
        btnUploadBarUpload.setTextColor(btnTextColor)
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
        commentDebouncer.setDelay(commentText, COMMENT_DEBOUNCE_DELAY) {}
    }

    private fun initUploadingBtnClickListener(textLength: Int, linkValidity: Boolean) {
        binding.layoutUploadBar.btnUploadBarUpload.setOnDuplicateBlockClick {
            if (textLength in POSTING_MIN..POSTING_MAX_WITH_LINK && linkValidity) {
                trackEvent(CLICK_REPLY_UPLOAD)
                homeViewModel.postCommentPosting(
                    contentId,
                    binding.etCommentContent.text.toString() + "\n" + binding.etCommentLink.text.toString()
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
