package com.teamdontbe.domain.entity

data class ProfileEditInfoEntity(
    val nickname: String,
    val isAlarmAllowed: Boolean?,
    val memberIntro: String,
)
