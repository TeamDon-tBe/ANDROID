package com.teamdontbe.feature.notification.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemNotificationFeedBinding
import com.teamdontbe.feature.util.CalculateTime
import timber.log.Timber

class NotificationViewHolder(
    private val binding: ItemNotificationFeedBinding,
    private val click: (NotiEntity, Int) -> Unit,
    private val onClickUserProfileBtn: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NotiEntity) {
        with(binding) {
            val spannableText = when (data.notificationTriggerType) {
                "contentLiked" -> {
                    initProfileBtnClickListener(data)
                    getSpannableText(
                        R.string.notification_feed_content_liked,
                        data = data
                    )
                }

                "comment" -> {
                    initProfileBtnClickListener(data)
                    getSpannableText(
                        R.string.notification_feed_comment,
                        data = data
                    )
                }

                "commentLiked" -> {
                    initProfileBtnClickListener(data)
                    getSpannableText(
                        R.string.notification_feed_comment_liked,
                        data = data
                    )
                }

                "actingContinue" -> getSpannableText(
                    R.string.notification_feed_acting_continue,
                    data = data
                )

                "beGhost" -> getSpannableText(
                    R.string.notification_feed_be_ghost,
                    data = data
                )

                "contentGhost" -> getSpannableText(
                    R.string.notification_feed_content_ghost,
                    data = data
                )

                "commentGhost" -> getSpannableText(
                    R.string.notification_feed_comment_ghost,
                    data = data
                )

                "userBan" -> getSpannableText(
                    R.string.notification_feed_user_ban,
                    36,
                    data = data
                )

                else -> {
                    Timber.tag("noti").e("등록되지 않은 노티가 감지되었습니다 : ${data.notificationTriggerType}")
                    SpannableString("")
                }
            }

            tvNotificationFeed.text = spannableText
            val timeUtil = CalculateTime(binding.root.context)
            tvNotificationTime.text = timeUtil.getCalculateTime(data.time)

            noti = data
            executePendingBindings()

            root.setOnClickListener {
                click(data, bindingAdapterPosition)
            }
        }
    }

    private fun initProfileBtnClickListener(data: NotiEntity) {
        binding.ivNotificationProfile.setOnClickListener {
            if (!data.isDeleted) onClickUserProfileBtn(data.triggerMemberId)
        }
    }

    private fun getSpannableText(
        resId: Int,
        endIndex: Int = 0,
        data: NotiEntity,
    ): SpannableStringBuilder {
        val text = data.triggerMemberNickname + binding.root.context.getString(resId)
        val spannableText = SpannableStringBuilder(text)
        if (data.notificationTriggerType == "contentLiked" || data.notificationTriggerType == "comment" || data.notificationTriggerType == "commentLiked") {
            spannableText.setSpan(
                clickableSpan(data),
                0,
                data.triggerMemberNickname.length + endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        spannableText.setSpan(
            StyleSpan(R.font.font_pretendard_semibold),
            0,
            data.triggerMemberNickname.length + endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        return spannableText
    }

    private fun clickableSpan(data: NotiEntity) = object : ClickableSpan() {
        override fun onClick(view: View) {
            if (!data.isDeleted) onClickUserProfileBtn(data.triggerMemberId)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
            ds.color = Color.parseColor("#FF49494C")
        }
    }
}
