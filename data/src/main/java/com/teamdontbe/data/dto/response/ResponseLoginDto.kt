package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.LoginEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginDto(
    @SerialName("nickName")
    val nickName: String,
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
    fun toLoginDataEntity() =
        LoginEntity(
            nickName,
            memberId,
            accessToken,
            refreshToken,
            isNewUser,
        )
}
