package com.teamdontbe.feature.notification.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemNotificationFeedBinding
import timber.log.Timber

class NotificationViewHolder(
    private val binding: ItemNotificationFeedBinding,
    private val click: (NotiEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NotiEntity) {
        with(binding) {
            when (data.notificationTriggerType) {
                "contentLiked" -> {
                    val a =
                        data.triggerMemberNickname + root.context.getString(R.string.notification_feed_content_liked)
                    val spannableText = SpannableString(a)
                    spannableText.setSpan(
                        TextAppearanceSpan(
                            root.context,
                            R.font.font_pretendard_semibold
                        ),
//                        StyleSpan(Typeface.BOLD),
                        0,
                        data.triggerMemberNickname.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                    tvNotificationFeed.text = spannableText
                }

                "comment" -> {
                    tvNotificationFeed.text =
                        data.triggerMemberNickname + root.context.getString(R.string.notification_feed_comment)
                }

                "commentLiked" -> {
                    tvNotificationFeed.text =
                        data.triggerMemberNickname + root.context.getString(R.string.notification_feed_comment_liked)
                }

                "actingContinue" -> {
                    tvNotificationFeed.text =
                        data.memberNickname + root.context.getString(R.string.notification_feed_acting_continue)
                }

                "beGhost" -> {
                    tvNotificationFeed.text =
                        data.memberNickname + root.context.getString(R.string.notification_feed_be_ghost)
                }

                "contentGhost" -> {
                    tvNotificationFeed.text =
                        data.memberNickname + root.context.getString(R.string.notification_feed_content_ghost)
                }

                "commentGhost" -> {
                    tvNotificationFeed.text =
                        data.memberNickname + root.context.getString(R.string.notification_feed_comment_ghost)
                }

                "userBan" -> {
                    tvNotificationFeed.text =
                        data.memberNickname + root.context.getString(R.string.notification_feed_user_ban)
                }

                else ->
                    Timber.tag("noti")
                        .e("등록되지 않은 노티가 감지되었습니다 : ${data.notificationTriggerType}")
            }
        }
    }
}
