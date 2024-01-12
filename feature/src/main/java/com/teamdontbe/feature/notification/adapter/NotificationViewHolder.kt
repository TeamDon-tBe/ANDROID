package com.teamdontbe.feature.notification.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemNotificationFeedBinding
import com.teamdontbe.feature.util.CalculateTime
import timber.log.Timber

class NotificationViewHolder(
    private val binding: ItemNotificationFeedBinding,
    private val click: (NotiEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NotiEntity) {
        with(binding) {
            val spannableText =
                when (data.notificationTriggerType) {
                    "contentLiked" ->
                        getSpannableText(
                            data.triggerMemberNickname,
                            R.string.notification_feed_content_liked,
                        )

                    "comment" ->
                        getSpannableText(
                            data.triggerMemberNickname,
                            R.string.notification_feed_comment,
                        )

                    "commentLiked" ->
                        getSpannableText(
                            data.triggerMemberNickname,
                            R.string.notification_feed_comment_liked,
                        )

                    "actingContinue" ->
                        getSpannableText(
                            data.memberNickname,
                            R.string.notification_feed_acting_continue,
                        )

                    "beGhost" ->
                        getSpannableText(
                            data.memberNickname,
                            R.string.notification_feed_be_ghost,
                        )

                    "contentGhost" ->
                        getSpannableText(
                            data.memberNickname,
                            R.string.notification_feed_content_ghost,
                        )

                    "commentGhost" ->
                        getSpannableText(
                            data.memberNickname,
                            R.string.notification_feed_comment_ghost,
                        )

                    "userBan" ->
                        getSpannableText(
                            data.memberNickname,
                            R.string.notification_feed_user_ban,
                            36,
                        )

                    else -> {
                        Timber.tag("noti")
                            .e("등록되지 않은 노티가 감지되었습니다 : ${data.notificationTriggerType}")
                        SpannableString("")
                    }
                }

            tvNotificationFeed.text = spannableText
            val timeUtil = CalculateTime(binding.root.context)
            tvNotificationTime.text = timeUtil.getCalculateTime(data.time)
        }
    }

    private fun getSpannableText(
        name: String,
        resId: Int,
        endIndex: Int = 0,
    ): SpannableString {
        val text = name + binding.root.context.getString(resId)
        val spannableText = SpannableString(text)
        spannableText.setSpan(
            StyleSpan(R.font.font_pretendard_semibold),
            0,
            name.length + endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        return spannableText
    }
}
