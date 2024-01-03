package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.SupportEntity
import com.teamdontbe.domain.entity.UserDataEntity
import com.teamdontbe.domain.entity.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("email") val email: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("avatar") val avatar: String,
) {
    fun toUserEntity() = UserEntity(
        id, email, firstName, lastName, avatar
    )
}

@Serializable
data class UserDataDto(
    @SerialName("page") val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total") val total: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("data") val data: List<User>,
    @SerialName("support") val support: Support,
) {
    fun toUserDataEntity() = UserDataEntity(
        page, perPage, total, totalPages, data.map { it.toUserEntity() }, support.toSupportEntity()
    )
}

@Serializable
data class Support(
    @SerialName("url") val url: String,
    @SerialName("text") val text: String,
) {
    fun toSupportEntity() = SupportEntity(
        url, text
    )
}