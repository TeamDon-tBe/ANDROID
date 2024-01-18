package com.teamdontbe.feature.mypage

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import com.teamdontbe.feature.home.HomeFragment.Companion.KEY_FEED_DATA
import com.teamdontbe.feature.mypage.bottomsheet.MyPageAnotherUserBottomSheet
import com.teamdontbe.feature.mypage.bottomsheet.MyPageBottomSheet
import com.teamdontbe.feature.mypage.transperencyinfo.TransparencyInfoParentFragment
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
        scrollRecyclerViewToTop()
    }

    private fun setUpMemberProfile(): MyPageModel {
        val memberProfile = MyPageModel(
            id = viewModel.getMemberId() ?: -1,
            nickName = getString(R.string.my_page_nickname),
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
        viewModel.getMyPageUserProfileState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> handleSuccessState(it.data, memberProfile)

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleSuccessState(data: MyPageUserProfileEntity, memberProfile: MyPageModel) =
        with(binding) {
            initMyPageProgressBarUI(data.memberGhost)
            tvMyPageTitle.text = data.nickname
            tvMyPageDescription.text = data.memberIntro
            if (data.memberProfileUrl == "") {
                ivMyPageProfile.setImageResource(R.drawable.ic_sign_up_profile_person)
            } else {
                loadImage(ivMyPageProfile, data.memberProfileUrl)
            }

            memberProfile.nickName = data.nickname
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
                (updateProgress * (progressBar.width - 2)) / progressBar.max - requireContext().pxToDp(
                    12,
                )
                ) - (progressLabelTextView.width / 2)
        val finalX =
            if (progressLabelTextView.width + textViewX > maxX) (maxX - progressLabelTextView.width - 16) else textViewX + 16 /*your margin*/

        progressLabelTextView.apply {
            x = if (finalX < 0) 16.toFloat() else finalX.toFloat()
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
                    isComment = false,
                    commentId = -1,
                ).show(childFragmentManager, MY_PAGE_BOTTOM_SHEET)
            }
        }
    }

    private fun scrollRecyclerViewToTop() {
        (requireActivity() as MainActivity).findViewById<BottomNavigationView>(R.id.bnv_main)
            .setOnItemReselectedListener { item ->
                if (item.itemId == R.id.fragment_my_page) {
                    val nestedScroll = binding.nestedScrollMyPage
                    binding.nestedScrollMyPage.post {
                        nestedScroll.smoothScrollTo(0, 0)
                    }
                }
            }
    }

    companion object {
        const val POSTING = "게시글"
        const val COMMENT = "답글"
        const val TRANSPARENCY_INFO = "TransparencyInfo"
        const val MY_PAGE_BOTTOM_SHEET = "MyPageBottomSheet"
    }

    override fun onResume() {
        val memberProfile: MyPageModel = setUpMemberProfile()
        initMyPageStateObserve(memberProfile)
        initMyPageTabLayout(memberProfile)
        super.onResume()
    }
}
