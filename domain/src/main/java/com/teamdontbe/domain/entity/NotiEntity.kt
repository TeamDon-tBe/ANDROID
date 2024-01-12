package com.teamdontbe.domain.entity

data class NotiEntity(
    val memberId: Int,
    val memberNickname: String,
    val triggerMemberNickname: String,
    val triggerMemberProfileUrl: String,
    val notificationTriggerType: String,
    val time: String,
    val notificationTriggerId: Int,
    val notificationText: String,
    val isNotificationChecked: Boolean,
)
