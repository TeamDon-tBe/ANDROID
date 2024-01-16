package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyPageUserProfileDto(
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("memberProfileUrl")
    val memberProfileUrl: String,
    @SerialName("memberIntro")
    val memberIntro: String,
    @SerialName("memberGhost")
    val memberGhost: Int,
) {
    fun toMyPageUserProfileEntity() = MyPageUserProfileEntity(
        memberId = memberId,
        nickname = nickname,
        memberProfileUrl = memberProfileUrl,
        memberIntro = memberIntro,
        memberGhost = memberGhost,
    )
}
