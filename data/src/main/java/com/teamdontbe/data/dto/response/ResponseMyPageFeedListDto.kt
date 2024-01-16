package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.MyPageFeeListEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyPageFeedListDto(
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("commentNumber")
    val commentNumber: Int,
    @SerialName("contentLikedNumber")
    val contentLikedNumber: Int,
    @SerialName("contentText")
    val contentText: String,
    @SerialName("isGhost")
    val isGhost: Boolean,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("memberGhost")
    val memberGhost: Int,
    @SerialName("memberNickname")
    val memberNickname: String,
    @SerialName("memberProfileUrl")
    val memberProfileUrl: String,
    @SerialName("time")
    val time: String,
) {
    fun toMyPageFeedListEntity() = MyPageFeeListEntity(
        memberId,
        commentNumber,
        contentLikedNumber,
        contentText,
        isGhost,
        isLiked,
        memberGhost,
        memberNickname,
        memberProfileUrl,
        time,
    )
}
