package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestComplaintDto(
    @SerialName("reportTargerNickname") val reportTargetNickname: String,
    @SerialName("relateText") val relateText: String,
)
