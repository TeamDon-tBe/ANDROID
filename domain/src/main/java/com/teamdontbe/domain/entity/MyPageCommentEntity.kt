package com.teamdontbe.domain.entity

data class MyPageCommentEntity(
    val memberId: Int,
    val memberProfileUrl: String,
    val memberNickname: String,
    val isGhost: Boolean,
    val memberGhost: Int,
    val isLiked: Boolean,
    val commentLikedNumber: Int,
    val commentText: String,
    val time: String,
    val commentId: Int,
    val contentId: Int,
)
