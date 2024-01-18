package com.teamdontbe.feature.mypage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyPageModel(
    var id: Int,
    var nickName: String,
    var idFlag: Boolean,
) : Parcelable
