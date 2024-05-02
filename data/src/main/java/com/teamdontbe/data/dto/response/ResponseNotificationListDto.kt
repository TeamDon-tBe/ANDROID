package com.teamdontbe.data.dto.response

import com.teamdontbe.domain.entity.NotiEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNotificationListDto(
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("memberNickname")
    val memberNickname: String,
    @SerialName("triggerMemberNickname")
    val triggerMemberNickname: String,
    @SerialName("triggerMemberProfileUrl")
    val triggerMemberProfileUrl: String,
    @SerialName("notificationTriggerType")
    val notificationTriggerType: String,
    @SerialName("time")
    val time: String,
    @SerialName("notificationTriggerId")
    val notificationTriggerId: Int,
    @SerialName("notificationText")
    val notificationText: String,
    @SerialName("isNotificationChecked")
    val isNotificationChecked: Boolean,
    @SerialName("isDeleted")
    val isDeleted: Boolean,
    @SerialName("notificationId")
    val notificationId: Int,
    @SerialName("triggerMemberId")
    val triggerMemberId: Int,
) {
    fun toNotificationEntity() =
        NotiEntity(
            memberId,
            memberNickname,
            triggerMemberNickname,
            triggerMemberProfileUrl,
            notificationTriggerType,
            time,
            notificationTriggerId,
            notificationText,
            isNotificationChecked,
            isDeleted,
            notificationId,
            triggerMemberId,
        )
}
