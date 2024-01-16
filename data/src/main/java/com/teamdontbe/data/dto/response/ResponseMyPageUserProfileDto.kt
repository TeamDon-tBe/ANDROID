package com.teamdontbe.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyPageUserProfileDto(
    @SerialName("memberGhost")
    val memberGhost: Int,
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("memberIntro")
    val memberIntro: String,
    @SerialName("memberProfileUrl")
    val memberProfileUrl: String,
    @SerialName("nickname")
    val nickname: String,
)
