package com.teamdontbe.feature.mypage.authinfo

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.toast
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserAccountInfoEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityMyPageAuthInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageAuthInfoActivity :
    BindingActivity<ActivityMyPageAuthInfoBinding>(R.layout.activity_my_page_auth_info) {
    private val viewModel: MyPageAuthInfoViewModel by viewModels()

    override fun initView() {
        binding.viewModel = viewModel

        binding.tvMyPageAuthInfoConditionsContent.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.tvMyPageAuthInfoWithdrawContent.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        initObserve()

        initConditionsBtnClickListener()
        initWithdrawBtnClickListener()
        initBackBtnClickListenr()
    }

    private fun initObserve() {
        viewModel.getMyPageFeedList()
        viewModel.getMyPageUserAccountInfoState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> handleSuccessState(it.data)

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleSuccessState(data: MyPageUserAccountInfoEntity) = with(binding) {
        tvMyPageAuthInfoDateContent.text = data.joinDate
        tvMyPageAuthInfoSocialContent.text = data.socialPlatform
        tvMyPageAuthInfoVersionContent.text = data.versionInformation
        tvMyPageAuthInfoIdContent.text = data.showMemberId
        tvMyPageAuthInfoVersionContent.text = data.versionInformation
    }

    private fun initWithdrawBtnClickListener() {
        binding.tvMyPageAuthInfoWithdrawContent.setOnClickListener {
            // 테스트용 코드
            toast("회원탈퇴 클릭됨")
        }
    }

    private fun initConditionsBtnClickListener() {
        binding.tvMyPageAuthInfoConditionsContent.setOnClickListener {
            navigateToComplaintWeb()
        }
    }

    private fun navigateToComplaintWeb() {
        val urlIntentComplaint =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/93625ba2f93547ff88984d3bb82a2f32"),
            )
        startActivity(urlIntentComplaint)
    }

    private fun initBackBtnClickListenr() {
        binding.ivMyPageAuthInfoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            // 예시: 이전 프레그먼트로 돌아가는 코드
            supportFragmentManager.popBackStack()
        }
    }
}
