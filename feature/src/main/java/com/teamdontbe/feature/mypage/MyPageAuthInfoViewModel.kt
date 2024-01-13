package com.teamdontbe.feature.mypage

import androidx.lifecycle.ViewModel
import com.teamdontbe.domain.entity.AuthInfoEntity

class MyPageAuthInfoViewModel : ViewModel() {
    val mockDataList =
        listOf(
            AuthInfoEntity(
                1,
                "2023.12.30",
                "example",
                "카카오톡 소셜 로그인",
                "V.1.0.01",
            ),
        )
}
