package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCommentLikedDto(
    @SerialName("notificationText") val notificationText: String,
    @SerialName("notificationTriggerType") val notificationTriggerType: String,
)
