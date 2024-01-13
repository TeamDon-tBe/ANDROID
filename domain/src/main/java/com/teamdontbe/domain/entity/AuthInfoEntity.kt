package com.teamdontbe.domain.entity

data class AuthInfoEntity(
    val memberId: Int,
    val joinDate: String,
    val showMemberId: String,
    val socialPlatform: String,
    val versionInformation: String,
)
