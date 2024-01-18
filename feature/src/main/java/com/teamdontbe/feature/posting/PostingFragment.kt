package com.teamdontbe.feature.posting

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.drawableOf
import com.teamdontbe.core_ui.util.context.openKeyboard
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentPostingBinding
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.HomePostingRestrictionDialogFragment
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
        requireContext().openKeyboard(binding.etPostingContent)
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
                    if (it.data.memberGhost == -85) {
                        val dialog = HomePostingRestrictionDialogFragment()
                        dialog.show(childFragmentManager, BAN_POSTING)
                    } else {
                        val inputMethodManager =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(
                            binding.etPostingContent,
                            InputMethodManager.SHOW_IMPLICIT,
                        )
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initUser() {
        binding.tvPostingProfileNickname.text = postingViewModel.getNickName()
        Timber.tag("user")
            .d("shared preference에서 받아오는 사용자 profile img url : ${postingViewModel.getMemberProfileUrl()}")
        binding.ivPostingProfileImg.load(postingViewModel.getMemberProfileUrl())
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
                when {
                    etPostingContent.text.toString().length in 1..499 -> {
                        pbPostingInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_primary_line_10_ring)
                        pbPostingInput.progress = etPostingContent.text.toString().length

                        btnPostingUpload.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.primary,
                                ),
                            )
                        btnPostingUpload.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.black,
                            ),
                        )
                        initUploadingActivateBtnClickListener()
                    }

                    etPostingContent.text.toString().length >= 500 -> {
                        pbPostingInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_error_line_10_ring)
                        pbPostingInput.progress = etPostingContent.text.toString().length

                        btnPostingUpload.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.gray_3,
                                ),
                            )
                        btnPostingUpload.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.gray_9,
                            ),
                        )
                        initUploadingDeactivateBtnClickListener()
                    }

                    else -> {
                        pbPostingInput.progress = 0
                        btnPostingUpload.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.gray_3,
                                ),
                            )
                        btnPostingUpload.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.gray_9,
                            ),
                        )
                        initUploadingDeactivateBtnClickListener()
                    }
                }
                postingDebouncer.setDelay(etPostingContent.text.toString(), 1000L) {}
            }
        }
    }

    companion object {
        const val DELETE_POSTING = "delete_posting"
        const val BAN_POSTING = "ban_posting"
    }
}
