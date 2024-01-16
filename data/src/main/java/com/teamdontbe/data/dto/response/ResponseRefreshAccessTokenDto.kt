package com.teamdontbe.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRefreshAccessTokenDto(
    @SerialName("accessToken")
    val accessToken: String,
)
