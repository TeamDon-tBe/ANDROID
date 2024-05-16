package com.teamdontbe.domain.entity

data class FeedEntity(
    val memberId: Int,
    val memberProfileUrl: String,
    val memberNickname: String,
    var isLiked: Boolean,
    val isGhost: Boolean,
    val memberGhost: Int,
    var contentLikedNumber: Int,
    val commentNumber: Int,
    val contentText: String,
    val time: String,
    val contentId: Int? = null,
    val isDeleted: Boolean? = null,
    val contentImageUrl: String? = null,
)
