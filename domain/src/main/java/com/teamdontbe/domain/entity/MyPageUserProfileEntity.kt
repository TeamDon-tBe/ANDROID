package com.teamdontbe.domain.entity

data class MyPageUserProfileEntity(
    val memberId: Int,
    val nickname: String,
    val memberProfileUrl: String,
    val memberIntro: String,
    val memberGhost: Int,
)

data class MyPageFeeListEntity(
    val memberId: Int,
    val commentNumber: Int,
    val contentLikedNumber: Int,
    val contentText: String,
    val isGhost: Boolean,
    val isLiked: Boolean,
    val memberGhost: Int,
    val memberNickname: String,
    val memberProfileUrl: String,
    val time: String,
)
