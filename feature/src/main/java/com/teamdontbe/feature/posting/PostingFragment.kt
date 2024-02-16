package com.teamdontbe.feature.posting

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.core_ui.util.fragment.colorOf
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentPostingBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.PostingRestrictionDialogFragment
import com.teamdontbe.feature.snackbar.UploadingSnackBar
import com.teamdontbe.feature.util.Debouncer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class PostingFragment : BindingFragment<FragmentPostingBinding>(R.layout.fragment_posting) {
    private val postingDebouncer = Debouncer<String>()
    private val postingViewModel by viewModels<PostingViewModel>()

    override fun initView() {
        statusBarColorOf(R.color.white)

        initAnimation()

        initObserveUser()
        initUser()
        initEditTextBtn()

        initCancelBtnClickListener()
        initObservePost()
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
                    } else {
                        showKeyboard()
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
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
                        UploadingSnackBar.make(binding.root).show(0, 0, 0, it.pxToDp(80))
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
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
        binding.btnPostingUpload.setOnClickListener {}
    }

    private fun initUploadingActivateBtnClickListener() {
        binding.btnPostingUpload.setOnClickListener {
            postingViewModel.posting(binding.etPostingContent.text.toString())
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
                val animateProgressBar =
                    AnimateProgressBar(
                        pbPostingInput,
                        0f,
                        etPostingContent.text.toString().length.toFloat(),
                    )

                when {
                    etPostingContent.text.toString().length in POSTING_MIN..POSTING_MAX -> {
                        updateProgress(
                            R.drawable.shape_primary_line_circle,
                            etPostingContent.text.toString().length,
                            R.color.primary,
                            R.color.black,
                        ) {
                            initUploadingActivateBtnClickListener()
                        }
                    }

                    etPostingContent.text.toString().length >= POSTING_MAX + 1 -> {
                        updateProgress(
                            R.drawable.shape_error_line_circle,
                            etPostingContent.text.toString().length,
                            R.color.gray_3,
                            R.color.gray_9,
                        ) {
                            initUploadingDeactivateBtnClickListener()
                        }
                    }

                    else -> {
                        updateProgress(
                            R.drawable.shape_primary_line_circle,
                            0,
                            R.color.gray_3,
                            R.color.gray_9,
                        ) {
                            initUploadingDeactivateBtnClickListener()
                        }
                    }
                }
                pbPostingInput.startAnimation(animateProgressBar)
                postingDebouncer.setDelay(etPostingContent.text.toString(), 1000L) {}
            }
        }
    }

    private fun FragmentPostingBinding.updateProgress(
        progressDrawableResId: Int,
        textLength: Int,
        backgroundTintResId: Int,
        textColorResId: Int,
        clickListener: () -> Unit,
    ) {
        pbPostingInput.progressDrawable = drawableOf(progressDrawableResId)
        pbPostingInput.progress = textLength

        btnPostingUpload.backgroundTintList =
            ColorStateList.valueOf(
                colorOf(backgroundTintResId),
            )
        btnPostingUpload.setTextColor(
            colorOf(textColorResId),
        )
        clickListener.invoke()
    }

    companion object {
        const val DELETE_POSTING = "delete_posting"
        const val BAN_POSTING = "ban_posting"
        const val POSTING_MIN = 1
        const val POSTING_MAX = 499
        const val TRANSPARENT_LIMIT = -85
    }
}
