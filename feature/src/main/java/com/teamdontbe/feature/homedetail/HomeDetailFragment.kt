package com.teamdontbe.feature.homedetail

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.comment.CommentBottomSheet
import com.teamdontbe.feature.databinding.FragmentHomeDetailBinding
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.home.HomeFragment.Companion.KEY_FEED_DATA

class HomeDetailFragment :
    BindingFragment<FragmentHomeDetailBinding>(R.layout.fragment_home_detail) {
    override fun initView() {
        statusBarColorOf(R.color.white)
        initHomeDetailAdapter()
        initBackBtnClickListener()
        (getHomeFeedDetailData())?.let { initInputEditTextClickListener(it) }
    }

    private fun initHomeDetailAdapter() {
        binding.rvHomeDetail.adapter =
            HomeDetailAdapter(onClickKebabBtn = { feedData, positoin ->
                initBottomSheet()
            }).apply {
                submitList(
                    listOf(
                        getHomeFeedDetailData()?.toFeedEntity(),
                        getHomeFeedDetailData()?.toFeedEntity(),
                        getHomeFeedDetailData()?.toFeedEntity(),
                        getHomeFeedDetailData()?.toFeedEntity(),
                        getHomeFeedDetailData()?.toFeedEntity(),
                        getHomeFeedDetailData()?.toFeedEntity(),
                        getHomeFeedDetailData()?.toFeedEntity(),
                    ),
                )
            }
        binding.rvHomeDetail.addItemDecoration(HomeDetailFeedItemDecorator(requireContext()))
    }

    private fun initBottomSheet() {
        HomeBottomSheet().show(parentFragmentManager, HomeFragment.HOME_BOTTOM_SHEET)
    }

    private fun getHomeFeedDetailData(): Feed? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_FEED_DATA, Feed::class.java)
        } else {
            requireArguments().getParcelable(KEY_FEED_DATA) as? Feed
        }

    private fun initBackBtnClickListener() {
        binding.ivHomeDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initInputEditTextClickListener(data: Feed) {
        binding.tvHomeDetailInput.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(KEY_FEED_DATA, data)
            val comment = CommentBottomSheet()
            comment.arguments = bundle
            comment.show(parentFragmentManager, COMMENT_BOTTOM_SHEET)
        }
    }

    companion object {
        const val COMMENT_BOTTOM_SHEET = "comment_bottom_sheet"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
