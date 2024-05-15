package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.CommentEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyPageCommentDto(
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("memberProfileUrl")
    val memberProfileUrl: String,
    @SerialName("memberNickname")
    val memberNickname: String,
    @SerialName("isGhost")
    val isGhost: Boolean,
    @SerialName("memberGhost")
    val memberGhost: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("commentLikedNumber")
    val commentLikedNumber: Int,
    @SerialName("commentText")
    val commentText: String,
    @SerialName("time")
    val time: String,
    @SerialName("commentId")
    val commentId: Int,
    @SerialName("contentId")
    val contentId: Int,
    @SerialName("commentImageUrl")
    val commentImageUrl: String? = null,
) {
    fun toMyPageCommentEntity() = CommentEntity(
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
        isDeleted = null,
        contentId,
        contentImageUrl = commentImageUrl,
    )
}
