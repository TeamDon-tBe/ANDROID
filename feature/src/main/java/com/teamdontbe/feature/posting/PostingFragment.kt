package com.teamdontbe.feature.posting

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.InputFilter
import android.util.Patterns.WEB_URL
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.AmplitudeUtil.trackEvent
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.core_ui.util.fragment.colorOf
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentPostingBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.PostingRestrictionDialogFragment
import com.teamdontbe.feature.snackbar.LinkCountErrorSnackBar
import com.teamdontbe.feature.snackbar.UploadingSnackBar
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_POST_UPLOAD
import com.teamdontbe.feature.util.Debouncer
import com.teamdontbe.feature.util.KeyStorage.BAN_POSTING
import com.teamdontbe.feature.util.KeyStorage.DELETE_POSTING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.regex.Pattern

@AndroidEntryPoint
class PostingFragment : BindingFragment<FragmentPostingBinding>(R.layout.fragment_posting) {
    private val postingDebouncer = Debouncer<String>()
    private val postingViewModel by viewModels<PostingViewModel>()
    private var totalContentLength = 0
    private var linkValidity = true
    private var linkLength = 0

    override fun initView() {
        statusBarColorOf(R.color.white)

        showKeyboard()
        initAnimation()

        initObserveUser()
        initUser()
        initEditTextBtn()

        initCancelBtnClickListener()
        initObservePost()

        initLinkBtnClickListener()
        initCancelLinkBtnClickListener()
        checkLinkValidity()
    }

    private fun initObserveUser() {
        postingViewModel.getMyPageUserProfileInfo(postingViewModel.getMemberId())
        postingViewModel.getMyPageUserProfileState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    if (it.data.memberGhost == TRANSPARENT_LIMIT) {
                        val dialog = PostingRestrictionDialogFragment()
                        dialog.show(childFragmentManager, BAN_POSTING)
                        hideKeyboard()
                    } else {
                        showKeyboard()
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etPostingContent.windowToken, 0)
    }

    private fun showKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            binding.etPostingContent,
            InputMethodManager.SHOW_IMPLICIT,
        )
        binding.etPostingContent.requestFocus()
    }

    private fun initUser() {
        binding.tvPostingProfileNickname.text = postingViewModel.getNickName()
        Timber.tag("user")
            .d("shared preference에서 받아오는 사용자 profile img url : ${postingViewModel.getMemberProfileUrl()}")

        if (postingViewModel.getMemberProfileUrl() == "") {
            binding.ivPostingProfileImg.load(
                """https:\\github.com\TeamDon-tBe\SERVER\assets\97835512\fb3ea04c-661e-4221-a837-854d66cdb77e""",
            )
        } else {
            binding.ivPostingProfileImg.load(postingViewModel.getMemberProfileUrl())
        }
    }

    private fun initAnimation() {
        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_posting_fragment_from_left)
        view?.startAnimation(animation)
    }

    private fun initObservePost() {
        postingViewModel.postPosting.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    navigateToMainActivity()
                    context?.let { it ->
                        UploadingSnackBar.make(binding.root)
                            .show(it.pxToDp(16), 0, it.pxToDp(16), it.pxToDp(80))
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initLinkBtnClickListener() = with(binding) {
        layoutUploadBar.ivUploadLink.setOnClickListener {
            handleUploadLinkClick()
        }
    }

    private fun handleUploadLinkClick() = with(binding) {
        if (totalContentLength > POSTING_MAX) return@with

        if (etPostingLink.isVisible) {
            LinkCountErrorSnackBar.make(binding.root)
                .show()
        } else {
            handleUploadProgressAndBtn(totalContentLength)
            setUploadingBtnValidity(false)
            linkValidity = false
        }
        handleLinkAndCancelBtnVisible(true)
        etPostingLink.requestFocus()
    }

    private fun initCancelLinkBtnClickListener() = with(binding) {
        ivPostingCancelLink.setOnClickListener {
            handleLinkAndCancelBtnVisible(false)
            etPostingLink.text.clear()
            etPostingContent.requestFocus()
            setLinkErrorMessageValidity(linkValidity = true)
            linkValidity = true
            handleUploadProgressAndBtn(totalContentLength)
            setContentMaxLength(POSTING_MAX - binding.etPostingLink.text.length + 1)
        }
    }

    private fun handleLinkAndCancelBtnVisible(isVisible: Boolean) = with(binding) {
        etPostingLink.isVisible = isVisible
        ivPostingCancelLink.isVisible = isVisible
    }

    private fun checkLinkValidity() = with(binding.etPostingLink) {
        doAfterTextChanged {
            setContentMaxLength(POSTING_MAX - binding.etPostingLink.text.length)
            handleLinkErrorMessage(WEB_URL_PATTERN.matcher(text.toString()).find())
            totalContentLength = binding.etPostingContent.text.length + text.length
            handleUploadProgressAndBtn(totalContentLength)
            postingDebouncer.setDelay(text.toString(), POSTING_DEBOUNCE_DELAY) {}
        }
    }

    private fun setContentMaxLength(length: Int) {
        binding.etPostingContent.filters = arrayOf(
            InputFilter.LengthFilter(length)
        )
    }

    private fun handleLinkErrorMessage(linkValidity: Boolean) {
        this.linkValidity = linkValidity
        setLinkErrorMessageValidity(linkValidity)
        setUploadingBtnValidity(linkValidity)
    }

    private fun setLinkErrorMessageValidity(linkValidity: Boolean) {
        binding.tvPostingLinkError.isVisible = !linkValidity
    }

    private fun setUploadingBtnValidity(linkValidity: Boolean) {
        if (linkValidity) {
            setUploadingBtnColor(
                R.color.primary,
                R.color.black,
            )
            initUploadingActivateBtnClickListener()
        } else {
            setUploadingBtnColor(
                R.color.gray_3,
                R.color.gray_9,
            )
            initUploadingDeactivateBtnClickListener()
        }
    }

    private fun initCancelBtnClickListener() {
        binding.appbarPosting.tvAppbarCancel.setOnClickListener {
            if (binding.etPostingContent.text.isNotEmpty()) {
                val dialog = DeleteDialogFragment(getString(R.string.posting_delete_dialog))
                dialog.show(childFragmentManager, DELETE_POSTING)
            } else {
                navigateToMainActivity()
            }
        }
    }

    private fun initUploadingDeactivateBtnClickListener() {
        binding.layoutUploadBar.btnUploadBarUpload.setOnClickListener {}
    }

    private fun initUploadingActivateBtnClickListener() {
        binding.layoutUploadBar.btnUploadBarUpload.setOnClickListener {
            trackEvent(CLICK_POST_UPLOAD)
            postingViewModel.posting(
                binding.etPostingContent.text.toString() + binding.etPostingLink.text.takeIf { it.isNotEmpty() }
                    ?.let { "\n$it" }
            )
        }
    }

    private fun navigateToMainActivity() {
        findNavController().navigate(
            R.id.action_posting_to_home,
        )
    }

    @SuppressLint("ResourceAsColor")
    private fun initEditTextBtn() {
        binding.run {
            etPostingContent.doAfterTextChanged {
                etPostingLink.filters =
                    arrayOf(InputFilter.LengthFilter(POSTING_MAX - etPostingContent.text.length + 1))
                totalContentLength = etPostingContent.text.length + linkLength
                handleUploadProgressAndBtn(totalContentLength)
            }
        }
    }

    private fun handleUploadProgressAndBtn(totalContentLength: Int) = with(binding) {
        when {
            (totalContentLength in POSTING_MIN..POSTING_MAX) && linkValidity -> {
                updateProgress(
                    R.color.gray_6,
                    R.color.primary,
                    R.color.black,
                ) {
                    initUploadingActivateBtnClickListener()
                }
            }

            totalContentLength >= POSTING_MAX + 1 -> {
                updateProgress(
                    R.color.error,
                    R.color.gray_3,
                    R.color.gray_9,
                ) {
                    initUploadingDeactivateBtnClickListener()
                }
            }

            else -> {
                updateProgress(
                    R.color.gray_6,
                    R.color.gray_3,
                    R.color.gray_9,
                ) {
                    initUploadingDeactivateBtnClickListener()
                }
            }
        }
        updateTextCount(totalContentLength)
        postingDebouncer.setDelay(etPostingContent.text.toString(), POSTING_DEBOUNCE_DELAY) {}
    }

    private fun updateProgress(
        countTextColorResId: Int,
        backgroundTintResId: Int,
        textColorResId: Int,
        clickListener: () -> Unit,
    ) {
        binding.layoutUploadBar.tvPostingTextCount.setTextColor(colorOf(countTextColorResId))
        setUploadingBtnColor(backgroundTintResId, textColorResId)
        clickListener.invoke()
    }

    private fun setUploadingBtnColor(
        backgroundTintResId: Int,
        textColorResId: Int,
    ) {
        binding.layoutUploadBar.btnUploadBarUpload.backgroundTintList = ColorStateList.valueOf(
            colorOf(backgroundTintResId),
        )
        binding.layoutUploadBar.btnUploadBarUpload.setTextColor(
            colorOf(textColorResId),
        )
    }

    private fun updateTextCount(totalCommentLength: Int) {
        binding.layoutUploadBar.tvPostingTextCount.text = getString(
            R.string.posting_text_count,
            totalCommentLength
        )
    }

    companion object {
        const val POSTING_MIN = 1
        const val POSTING_MAX = 499
        const val TRANSPARENT_LIMIT = -85
        const val POSTING_DEBOUNCE_DELAY = 1000L
        val WEB_URL_PATTERN: Pattern = WEB_URL
    }
}
