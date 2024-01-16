package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFeedDto(
    @SerialName("memberId") val memberId: Int,
    @SerialName("memberProfileUrl") val memberProfileUrl: String,
    @SerialName("memberNickname") val memberNickname: String,
    @SerialName("contentId") val contentId: Int? = null,
    @SerialName("contentText") val contentText: String,
    @SerialName("time") val time: String,
    @SerialName("isGhost") val isGhost: Boolean,
    @SerialName("memberGhost") val memberGhost: Int,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("likedNumber") val likedNumber: Int,
    @SerialName("commentNumber") val commentNumber: Int,
) {
    fun toFeedEntity() =
        FeedEntity(
            memberId,
            memberProfileUrl,
            memberNickname,
            isLiked,
            isGhost,
            memberGhost,
            likedNumber,
            commentNumber,
            contentText,
            time,
        )
}
