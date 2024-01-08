package com.teamdontbe.feature.mypage.adapter

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class MyPageFeedViewHolder(private val binding: ItemHomeFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: UserEntity) = with(binding) {
        tvHomeFeedUserName.text = "${data.lastName} ${data.firstName}"
        tvHomeFeedContent.text =
            "돈비를 사용하면 진짜 돈비를 맞을 수 있나요? 저 돈비 맞고 싶어요 돈벼락이 최고입니다 . 그나저나 돈비 정말 흥미로운 서비스인 것 같아요 어떻게 이런 기획을 ? 대박 ㄷ ㄷ ㄷ돈비를 사용하면 진짜 돈비를 맞을 수 있나요?"
    }
}
