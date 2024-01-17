package com.teamdontbe.feature.mypage

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import com.teamdontbe.feature.mypage.bottomsheet.MyPageBottomSheet
import com.teamdontbe.feature.mypage.transperencyinfo.TransparencyInfoParentFragment
import com.teamdontbe.feature.notification.NotificationFragment
import com.teamdontbe.feature.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<MyPageViewModel>()
    private lateinit var memberProfile: MyPageModel

    override fun initView() {
        initMemberProfile()
        initMyPageCollapseAppearance()
        initMyPageStateObserve()
        initMyPageTabLayout()
        initBtnClickListener()
    }

    private fun initMemberProfile() {
        memberProfile = MyPageModel(
            id = viewModel.getMemberId() ?: -1,
            nickName = getString(R.string.my_page_nickname),
            idFlag = true,
        )

        arguments?.let {
            val parentData = it.getInt(NotificationFragment.KEY_NOTI_DATA, -1)
            if (memberProfile.id != parentData) {
                memberProfile.idFlag = false
                memberProfile.id = parentData
            }
        }
    }

    private fun initMyPageCollapseAppearance() {
        with(binding) {
            btnMyPageBack.visibility = View.INVISIBLE
            collapseMyPage.setContentScrimColor(requireContext().getColor(R.color.black))
            statusBarColorOf(R.color.black)
        }
    }

    private fun initMyPageStateObserve() {
        viewModel.getMyPageUserProfileInfo(memberProfile.id)
        viewModel.getMyPageUserProfileState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> handleSuccessState(it.data)
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleSuccessState(data: MyPageUserProfileEntity) {
        with(binding) {
            initMyPageProgressBarUI(data.memberGhost)
            tvMyPageTitle.text = data.nickname
            tvMyPageDescription.text = data.memberIntro
            loadImage(ivMyPageProfile, data.memberProfileUrl)
            memberProfile.nickName = data.nickname // 넘겨줄 nickname 추가
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

        val textViewX = (
            (updateProgress * (progressBar.width - 2)) / progressBar.max -
                requireContext().pxToDp(12)
            ) - (progressLabelTextView.width / 2)
        val finalX =
            if (progressLabelTextView.width + textViewX > maxX) (maxX - progressLabelTextView.width - 16) else textViewX + 16 /*your margin*/

        progressLabelTextView.apply {
            x = if (finalX < 0) 16.toFloat() else finalX.toFloat()
            text = "$progressStatus%"
        }
    }

    private fun initMyPageTabLayout() {
        with(binding) {
            vpMyPage.adapter = MyPageVpAdapter(this@MyPageFragment, memberProfile)

            val tabTitleArray = arrayOf(
                POSTING,
                COMMENT,
            )

            TabLayoutMediator(tabMyPage, vpMyPage) { tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        }
    }

    private fun initBtnClickListener() {
        initTransparencyInfoDialogBtnClickListener()
        initMyPageHamburgerClickListener()
    }

    private fun initTransparencyInfoDialogBtnClickListener() {
        binding.btnMyPageTransparencyInfo.setOnClickListener {
            TransparencyInfoParentFragment().show(childFragmentManager, TRANSPARENCY_INFO)
        }
    }

    private fun initMyPageHamburgerClickListener() {
        binding.btnMyPageHamberger.setOnClickListener {
            MyPageBottomSheet().show(childFragmentManager, MY_PAGE_BOTTOM_SHEET)
        }
    }

    companion object {
        const val POSTING = "게시글"
        const val COMMENT = "답글"
        const val TRANSPARENCY_INFO = "TransparencyInfo"
        const val MY_PAGE_BOTTOM_SHEET = "MyPageBottomSheet"
    }
}
