package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestProfileEditDto(
    @SerialName("nickname") val nickName: String,
    @SerialName("is_alarm_allowed") val isAlarmAllowed: Boolean?,
    @SerialName("member_intro") val memberIntro: String,
    @SerialName("profile_url") val profileUrl: String?,
    @SerialName("isPushAlarmAllowed") val isPushAlarmAllowed: Boolean? = null,
)
