package com.teamdontbe.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotificationCountDto(
    @SerialName("notificationNumber")
    val notificationNumber: Int,
)