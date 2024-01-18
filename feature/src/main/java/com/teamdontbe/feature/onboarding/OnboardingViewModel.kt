package com.teamdontbe.feature.onboarding

import androidx.lifecycle.ViewModel
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val userInfoRepository: UserInfoRepository,
    ) : ViewModel() {
        fun getIsNewUser() = userInfoRepository.getIsNewUser()

        fun saveCheckLogin(checkLogin: Boolean) = userInfoRepository.saveCheckLogin(checkLogin)
    }
