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
) {
    fun toNotificationListEntity() =
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
        )
}
