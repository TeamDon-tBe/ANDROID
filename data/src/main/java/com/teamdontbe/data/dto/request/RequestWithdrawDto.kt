package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RequestWithdrawDto(
    @SerialName("withdrawalReason")
    val withdrawalReason: String,
)
