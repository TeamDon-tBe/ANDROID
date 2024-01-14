package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.AuthEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("memberProfileUrl")
    val memberProfileUrl: String,
    @SerialName("isNewUser")
    val isNewUser: Boolean,
) {
    fun toAuthDataEntity() = AuthEntity(
        nickname, memberId, accessToken, refreshToken, isNewUser
    )
}
