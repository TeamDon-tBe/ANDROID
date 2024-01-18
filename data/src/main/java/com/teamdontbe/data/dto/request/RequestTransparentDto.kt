package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTransparentDto(
    @SerialName("alarmTriggerType") val alarmTriggerType: String,
    @SerialName("targetMemberId") val targetMemberId: Int,
    @SerialName("alarmTriggerId") val alarmTriggerId: Int,
)
