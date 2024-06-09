package com.teamdontbe.domain.entity

data class ProfileEditInfoEntity(
    val nickname: String? = null,
    val isAlarmAllowed: Boolean? = null,
    val memberIntro: String? = null,
    val isPushAlarmAllowed: Boolean? = null,
    val fcmToken: String? = null
)
