package com.teamdontbe.feature.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R

@BindingAdapter("imageUrl")
fun loadImage(
    view: ImageView,
    url: String?,
) {
    if (url?.isBlank() == true) {
        view.load(R.drawable.ic_sign_up_profile_person)
    } else {
        view.load(url) {
            placeholder(R.drawable.ic_sign_up_profile_person) // 기본 이미지 설정
        }
    }
}

@BindingAdapter("setCircleImage")
fun ImageView.setCircleImage(img: String?) {
    load(img) {
        transformations(RoundedCornersTransformation(1000f))
    }
}

@BindingAdapter("setTransparencyAndTimeText")
fun TextView.setTransparencyAndTimeText(data: Any?) {
    data ?: return
    when (data) {
        is FeedEntity -> {
            if (data.time.isNotEmpty()) {
                text = context.getString(
                    R.string.tv_transparency,
                    data.memberGhost,
                    CalculateTime(context).getCalculateTime(data.time),
                )
            }
        }

        is CommentEntity -> {
            if (data.time.isNotEmpty()) {
                text = context.getString(
                    R.string.tv_transparency,
                    data.memberGhost,
                    CalculateTime(context).getCalculateTime(data.time),
                )
            }
        }
    }
}

@BindingAdapter("setEmptyImageUrl")
fun ImageView.setEmptyImageUrl(img: String?) {
    if (img.isNullOrEmpty()) {
        this.isGone = true
    } else {
        this.isVisible = true
        load(img)
    }
}
