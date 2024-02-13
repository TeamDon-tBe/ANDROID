package com.teamdontbe.feature.signup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileModel(
    val nickName: String,
    val allowedCheck: Boolean,
    val introduce: String,
    val imgUrl: String?,
) : Parcelable
