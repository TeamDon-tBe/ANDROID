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
import com.teamdontbe.core_ui.view.setOnDuplicateBlockClick
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetCommentBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.homedetail.HomeDetailFragment.Companion.COMMENT_DEBOUNCE_DELAY
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_DEBOUNCE_DELAY
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_MAX
import com.teamdontbe.feature.posting.PostingFragment.Companion.POSTING_MIN
import com.teamdontbe.feature.posting.PostingFragment.Companion.WEB_URL_PATTERN
import com.teamdontbe.feature.snackbar.LinkCountErrorSnackBar
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
    private var linkLength = 0

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
            handleUploadLinkClick()
        }
    }

    private fun handleUploadLinkClick() = with(binding) {
        if (totalCommentLength > POSTING_MAX) return@with

        if (etCommentLink.isVisible) LinkCountErrorSnackBar.make(binding.root)
            .show() else linkValidity = false
        handleLinkAndCancelBtnVisible(true)
        etCommentLink.requestFocus()
        setUploadingCommentState(totalCommentLength)
    }

    private fun initCancelLinkBtnClickListener() = with(binding) {
        ivCommentCancelLink.setOnClickListener {
            handleLinkAndCancelBtnVisible(false)
            etCommentLink.text.clear()
            etCommentContent.requestFocus()
            setLinkErrorMessageValidity(linkValidity = true)
            linkValidity = true
            setUploadingCommentState(totalCommentLength)
            setCommentMaxLength(POSTING_MAX - binding.etCommentLink.text.length + 1)
        }
    }

    private fun handleLinkAndCancelBtnVisible(isVisible: Boolean) = with(binding) {
        etCommentLink.isVisible = isVisible
        ivCommentCancelLink.isVisible = isVisible
    }

    private fun checkLinkValidity() = with(binding.etCommentLink) {
        doAfterTextChanged {
            setCommentMaxLength(POSTING_MAX - binding.etCommentLink.text.length)
            totalCommentLength = binding.etCommentContent.length() + text.length
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

    private fun setCommentMaxLength(length: Int) {
        binding.etCommentContent.filters = arrayOf(
            InputFilter.LengthFilter(length)
        )
    }

    private fun handleLinkErrorMessage(linkValidity: Boolean) {
        this.linkValidity = linkValidity
        setLinkErrorMessageValidity(linkValidity)
    }

    private fun setLinkErrorMessageValidity(linkValidity: Boolean) {
        binding.tvCommentLinkError.isVisible = !linkValidity
    }

    private fun initEditText() = with(binding) {
        etCommentContent.doAfterTextChanged { text ->
            etCommentLink.filters =
                arrayOf(InputFilter.LengthFilter(POSTING_MAX - text.toString().length + 1))
            totalCommentLength = etCommentContent.text.length + linkLength
            setUploadingCommentState(totalCommentLength)
            debounceComment(text.toString())
        }
    }

    private fun setUploadingCommentState(totalCommentLength: Int) {
        val countTextColorId = getCountTextColorId(totalCommentLength)
        val btnBackgroundTint = getButtonBackgroundTint(totalCommentLength)
        val btnTextColor = getButtonTextColor(totalCommentLength)

        updateTextCount(totalCommentLength)
        updateUploadBar(countTextColorId, btnBackgroundTint, btnTextColor)
        initUploadingBtnClickListener(totalCommentLength, linkValidity)
    }

    private fun updateTextCount(totalCommentLength: Int) {
        binding.layoutUploadBar.tvPostingTextCount.text = getString(
            R.string.posting_text_count,
            totalCommentLength
        )
    }

    private fun getCountTextColorId(textLength: Int): Int =
        if (textLength > POSTING_MAX) R.color.error else R.color.gray_6

    private fun getButtonBackgroundTint(textLength: Int): ColorStateList =
        if (textLength in POSTING_MIN..POSTING_MAX && linkValidity) {
            ColorStateList.valueOf(
                colorOf(R.color.primary),
            )
        } else {
            ColorStateList.valueOf(colorOf(R.color.gray_3))
        }

    private fun getButtonTextColor(textLength: Int): Int =
        if (textLength in POSTING_MIN..POSTING_MAX && linkValidity) {
            colorOf(R.color.black)
        } else {
            colorOf(
                R.color.gray_9,
            )
        }

    private fun updateUploadBar(
        countTextColorResId: Int,
        btnBackgroundTint: ColorStateList,
        btnTextColor: Int,
    ) {
        binding.layoutUploadBar.tvPostingTextCount.setTextColor(colorOf(countTextColorResId))
        setUploadBtnColor(btnBackgroundTint, btnTextColor)
    }

    private fun setUploadBtnColor(
        btnBackgroundTint: ColorStateList,
        btnTextColor: Int,
    ) = with(binding.layoutUploadBar) {
        btnUploadBarUpload.backgroundTintList = btnBackgroundTint
        btnUploadBarUpload.setTextColor(btnTextColor)
    }

    private fun debounceComment(commentText: String) {
        commentDebouncer.setDelay(commentText, COMMENT_DEBOUNCE_DELAY) {}
    }

    private fun initUploadingBtnClickListener(textLength: Int, linkValidity: Boolean) {
        binding.layoutUploadBar.btnUploadBarUpload.setOnDuplicateBlockClick {
            if (textLength in POSTING_MIN..POSTING_MAX && linkValidity) {
                trackEvent(CLICK_REPLY_UPLOAD)
                homeViewModel.postCommentPosting(
                    contentId,
                    binding.etCommentContent.text.toString() + binding.etCommentLink.text.takeIf { it.isNotEmpty() }
                        ?.let { "\n$it" }
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
