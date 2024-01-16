package com.teamdontbe.domain.entity

data class MyPageUserProfileEntity(
    val memberId: Int,
    val nickname: String,
    val memberProfileUrl: String,
    val memberIntro: String,
    val memberGhost: Int,
)
