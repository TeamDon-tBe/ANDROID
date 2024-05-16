package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.CommentEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCommentDto(
    @SerialName("memberId") val memberId: Int,
    @SerialName("memberProfileUrl") val memberProfileUrl: String,
    @SerialName("memberNickname") val memberNickname: String,
    @SerialName("isGhost") val isGhost: Boolean,
    @SerialName("memberGhost") val memberGhost: Int,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("commentLikedNumber") val commentLikedNumber: Int,
    @SerialName("commentText") val commentText: String,
    @SerialName("time") val time: String,
    @SerialName("commentId") val commentId: Int,
    @SerialName("isDeleted") val isDeleted: Boolean? = null,
    @SerialName("commentImageUrl") val commentImageUrl: String? = null,
) {
    fun toCommentEntity() =
        CommentEntity(
            memberId,
            memberProfileUrl,
            memberNickname,
            isGhost,
            memberGhost,
            isLiked,
            commentLikedNumber,
            commentText,
            time,
            commentId,
            isDeleted,
            commentImageUrl = commentImageUrl,
        )
}
