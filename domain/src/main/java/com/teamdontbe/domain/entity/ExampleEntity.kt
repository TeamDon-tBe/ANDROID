package com.teamdontbe.domain.entity

data class UserEntity(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
)

data class UserDataEntity(
    val page: Int = 1,
    val perPage: Int = 1,
    val total: Int = 1,
    val totalPages: Int = 1,
    val data: List<UserEntity>,
    val support: SupportEntity,
)

data class SupportEntity(
    val url: String,
    val text: String,
)