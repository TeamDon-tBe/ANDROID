package com.teamdontbe.feature.mypage

import android.graphics.Paint
import androidx.fragment.app.viewModels
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.toast
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageAuthInfoBinding

class MyPageAuthInfoFragment :
    BindingFragment<FragmentMyPageAuthInfoBinding>(R.layout.fragment_my_page_auth_info) {
    private val viewModel: MyPageAuthInfoViewModel by viewModels()

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tvMyPageAuthInfoConditionsContent.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.tvMyPageAuthInfoWithdrawContent.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        initConditionsBtnClickListener()
        initWithdrawBtnClickListener()
    }

    private fun initWithdrawBtnClickListener() {
        binding.tvMyPageAuthInfoWithdrawContent.setOnClickListener {
            // 테스트용 코드
            context?.toast("회원탈퇴 클릭됨")
        }
    }

    private fun initConditionsBtnClickListener() {
        binding.tvMyPageAuthInfoConditionsContent.setOnClickListener {
            // 테스트용 코드
            context?.toast("자세히보기 클릭됨")
        }
    }
}
