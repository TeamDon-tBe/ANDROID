package com.teamdontbe.feature.home

import android.os.Parcelable
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    val memberId: Int,
    val memberProfileUrl: String,
    val memberNickname: String,
    val isLiked: Boolean,
    val isGhost: Boolean,
    val memberGhost: Int,
    val contentLikedNumber: Int,
    val commentNumber: Int,
    val contentText: String,
    val time: String,
    val contentId: Int? = null,
    val isDeleted: Boolean? = null,
    val contentImageUrl: String? = null,
) : Parcelable {
    constructor(feedEntity: FeedEntity) : this(
        feedEntity.memberId,
        feedEntity.memberProfileUrl,
        feedEntity.memberNickname,
        feedEntity.isLiked,
        feedEntity.isGhost,
        feedEntity.memberGhost,
        feedEntity.contentLikedNumber,
        feedEntity.commentNumber,
        feedEntity.contentText,
        feedEntity.time,
        feedEntity.contentId,
        feedEntity.isDeleted,
        feedEntity.contentImageUrl,
    )

    fun toFeedEntity() =
        FeedEntity(
            memberId,
            memberProfileUrl,
            memberNickname,
            isLiked,
            isGhost,
            memberGhost,
            contentLikedNumber,
            commentNumber,
            contentText,
            time,
            contentId,
            isDeleted,
            contentImageUrl,
        )
}
