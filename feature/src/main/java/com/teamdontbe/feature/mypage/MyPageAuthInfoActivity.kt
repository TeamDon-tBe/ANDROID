package com.teamdontbe.feature.mypage

import android.graphics.Paint
import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.toast
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityMyPageAuthInfoBinding

class MyPageAuthInfoActivity :
    BindingActivity<ActivityMyPageAuthInfoBinding>(R.layout.activity_my_page_auth_info) {
    private val viewModel: MyPageAuthInfoViewModel by viewModels()

    override fun initView() {
        binding.viewModel = viewModel

        binding.tvMyPageAuthInfoConditionsContent.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.tvMyPageAuthInfoWithdrawContent.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        initConditionsBtnClickListener()
        initWithdrawBtnClickListener()
    }

    private fun initWithdrawBtnClickListener() {
        binding.tvMyPageAuthInfoWithdrawContent.setOnClickListener {
            // 테스트용 코드
            toast("회원탈퇴 클릭됨")
        }
    }

    private fun initConditionsBtnClickListener() {
        binding.tvMyPageAuthInfoConditionsContent.setOnClickListener {
            // 테스트용 코드
            toast("자세히보기 클릭됨")
        }
    }
}
