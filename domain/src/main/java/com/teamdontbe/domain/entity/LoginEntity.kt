package com.teamdontbe.domain.entity

data class LoginEntity(
    val nickname: String,
    val memberId: Int,
    val accessToken: String,
    val refreshToken: String,
    val memberProfileUrl: String,
    val isNewUser: Boolean,
    val isPushAlarmAllowed: Boolean?,
)
