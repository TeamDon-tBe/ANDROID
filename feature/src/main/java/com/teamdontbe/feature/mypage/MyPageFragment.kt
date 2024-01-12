package com.teamdontbe.feature.mypage

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.pxToDp
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun initView() {
        initMyPageCollapseAppearance()
        initMyPageProgressBarUI()
        initMyPageTabLayout()
    }

    private fun initMyPageCollapseAppearance() = with(binding) {
        btnMyPageBack.visibility = View.INVISIBLE
        collapseMyPage.setContentScrimColor(requireContext().getColor(R.color.black))
    }

    private fun initMyPageProgressBarUI() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            updateProgressWithLabel(
                binding.pbMyPageInput,
                30,
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
        progressBar.progress = progressStatus

        val textViewX = (
            (progressStatus * (progressBar.width - 2)) / progressBar.max -
                requireContext().pxToDp(12)
            ) - (progressLabelTextView.width / 2)
        val finalX =
            if (progressLabelTextView.width + textViewX > maxX) (maxX - progressLabelTextView.width - 16) else textViewX + 16 /*your margin*/

        progressLabelTextView.apply {
            x = if (finalX < 0) 16.toFloat() else finalX.toFloat()
            text = "-$progressStatus%"
        }
    }

    private fun initMyPageTabLayout() = with(binding) {
        vpMyPage.adapter = MyPageVpAdapter(this@MyPageFragment)

        val tabTitleArray = arrayOf(
            POSTING,
            COMMENT,
        )

        TabLayoutMediator(tabMyPage, vpMyPage) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    companion object {
        const val POSTING = "게시글"
        const val COMMENT = "답글"
    }
}
