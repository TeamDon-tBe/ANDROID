package com.teamdontbe.feature.mypage

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.feature.ErrorActivity.Companion.navigateToErrorPage
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import com.teamdontbe.feature.mypage.bottomsheet.MyPageAnotherUserBottomSheet
import com.teamdontbe.feature.mypage.bottomsheet.MyPageBottomSheet
import com.teamdontbe.feature.mypage.feed.MyPageFeedFragment.Companion.FROM_FEED
import com.teamdontbe.feature.mypage.transperencyinfo.TransparencyInfoParentFragment
import com.teamdontbe.feature.util.KeyStorage.KEY_FEED_DATA
import com.teamdontbe.feature.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<MyPageViewModel>()

    override fun initView() {
        binding.btnMyPageBack.visibility = View.INVISIBLE

        val memberProfile: MyPageModel = setUpMemberProfile()

        initMyPageCollapseAppearance()
        initMyPageStateObserve(memberProfile)
        initMyPageTabLayout(memberProfile)
        initMyPageHambergerClickListner(memberProfile)
        initTransparencyInfoDialogBtnClickListener()
        initBackBtnClickListener()
    }

    private fun setUpMemberProfile(): MyPageModel {
        val memberProfile = MyPageModel(
            id = viewModel.getMemberId() ?: -1,
            nickName = viewModel.getUserNickName() ?: getString(R.string.my_page_nickname),
            idFlag = true,
        )
        arguments?.let {
            val parentData = it.getInt(KEY_FEED_DATA, -1)
            setUpCaseProcessing(memberProfile, parentData)
        }
        return memberProfile
    }

    private fun setUpCaseProcessing(
        memberProfile: MyPageModel,
        parentData: Int,
    ) {
        if (memberProfile.id != parentData) {
            memberProfile.idFlag = false
            memberProfile.id = parentData
            (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
                View.GONE
            binding.btnMyPageBack.visibility = View.VISIBLE
        }
    }

    private fun initMyPageCollapseAppearance() = with(binding) {
        collapseMyPage.setContentScrimColor(requireContext().getColor(R.color.black))
        statusBarColorOf(R.color.black)
    }

    private fun initMyPageStateObserve(memberProfile: MyPageModel) {
        viewModel.getMyPageUserProfileInfo(memberProfile.id)
        viewModel.getMyPageUserProfileState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            when (it) {
                is UiState.Success -> handleSuccessState(it.data, memberProfile)
                is UiState.Failure -> navigateToErrorPage(requireContext())
                else -> Unit
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleSuccessState(data: MyPageUserProfileEntity, memberProfile: MyPageModel) {
        initMyPageProgressBarUI(data.memberGhost)
        setProfileData(data)
        updateImageUrlIfChanged(data, memberProfile)
    }

    private fun setProfileData(data: MyPageUserProfileEntity) = with(binding) {
        tvMyPageTitle.text = data.nickname
        tvMyPageDescription.text = data.memberIntro
        setProfileImage(data.memberProfileUrl)
    }

    private fun setProfileImage(url: String) = with(binding) {
        if (url == "") {
            ivMyPageProfile.setImageResource(R.drawable.ic_sign_up_profile_person)
        } else {
            loadImage(ivMyPageProfile, url)
        }
    }

    private fun updateImageUrlIfChanged(data: MyPageUserProfileEntity, memberProfile: MyPageModel) {
        if (memberProfile.idFlag) {
            viewModel.updateImageUrl(data.memberProfileUrl)
        }
    }

    private fun initMyPageProgressBarUI(progressTransparency: Int) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            updateProgressWithLabel(
                binding.pbMyPageInput,
                progressTransparency,
                binding.tvMyPageTransparencyPercentage,
                Resources.getSystem().displayMetrics.widthPixels,
            )
        }
    }

    private fun updateProgressWithLabel(
        progressBar: ProgressBar,
        progressStatus: Int,
        progressLabelTextView: TextView,
        maxX: Int,
    ) {
        val updateProgress = progressStatus + 100
        progressBar.progress = updateProgress

        val textViewX =
            (
                (updateProgress * (progressBar.width - PROGRESSBAR_RADIUS_OFFSET)) / progressBar.max - requireContext().pxToDp(
                    PROGRESS_LABEL_OFFSET,
                )
                ) - (progressLabelTextView.width / PROGRESSBAR_RADIUS_OFFSET)
        val finalX =
            if (progressLabelTextView.width + textViewX > maxX) (maxX - progressLabelTextView.width - LAYOUT_MARGIN) else textViewX + LAYOUT_MARGIN

        progressLabelTextView.apply {
            x = if (finalX < 0) LAYOUT_MARGIN.toFloat() else finalX.toFloat()
            text = "$progressStatus%"
        }
    }

    private fun initMyPageTabLayout(memberProfile: MyPageModel) = with(binding) {
        vpMyPage.adapter = MyPageVpAdapter(this@MyPageFragment, memberProfile)

        val tabTitleArray = arrayOf(
            POSTING,
            COMMENT,
        )

        TabLayoutMediator(tabMyPage, vpMyPage) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    private fun initTransparencyInfoDialogBtnClickListener() {
        binding.btnMyPageTransparencyInfo.setOnClickListener {
            TransparencyInfoParentFragment().show(childFragmentManager, TRANSPARENCY_INFO)
        }
    }

    private fun initMyPageHambergerClickListner(memberProfile: MyPageModel) {
        binding.btnMyPageHamberger.setOnClickListener {
            if (memberProfile.idFlag) {
                MyPageBottomSheet().show(childFragmentManager, MY_PAGE_BOTTOM_SHEET)
            } else {
                MyPageAnotherUserBottomSheet(
                    isMember = memberProfile.idFlag,
                    contentId = memberProfile.id,
                    commentId = -1,
                    whereFrom = FROM_FEED,
                ).show(childFragmentManager, MY_PAGE_ANOTHER_BOTTOM_SHEET)
            }
        }
    }

    private fun initBackBtnClickListener() {
        binding.btnMyPageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun updateUI() {
        val memberProfile: MyPageModel = setUpMemberProfile()
        initMyPageStateObserve(memberProfile)
        initMyPageTabLayout(memberProfile)
    }

    companion object {
        const val POSTING = "게시글"
        const val COMMENT = "답글"
        const val TRANSPARENCY_INFO = "TransparencyInfo"
        const val MY_PAGE_BOTTOM_SHEET = "MyPageBottomSheet"
        const val MY_PAGE_ANOTHER_BOTTOM_SHEET = "MyPageAnotherBottomSheet"
        const val PROGRESSBAR_RADIUS_OFFSET = 2
        const val PROGRESS_LABEL_OFFSET = 12
        const val LAYOUT_MARGIN = 16
    }
}
