package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCommentPostingDto(
    @SerialName("commentText") val commentTex: String,
    @SerialName("notificationTriggerType") val notificationTriggerType: String,
)
