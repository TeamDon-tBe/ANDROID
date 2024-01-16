package com.teamdontbe.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostingDto(
    @SerialName("contentText")
    val contentText: String,
)
