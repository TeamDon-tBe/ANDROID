package com.teamdontbe.domain.entity

data class CommentEntity(
    val memberId: Int,
    val memberProfileUrl: String,
    val memberNickname: String,
    val isGhost: Boolean,
    val memberGhost: Int,
    val isLiked: Boolean,
    val commentLikedNumber: Int,
    val contentText: String,
    val time: String,
)
