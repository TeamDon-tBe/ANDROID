package com.teamdontbe.feature.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.repository.LoginRepository
import com.teamdontbe.domain.repository.PostingRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
@Inject
constructor(
    private val postingRepository: PostingRepository,
    private val loginRepository: LoginRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _postPosting = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val postPosting: StateFlow<UiState<Boolean>> = _postPosting

    private val _introduction = MutableLiveData("")
    val introduction: LiveData<String> get() = _introduction

    private val _profileEditSuccess = MutableLiveData<Boolean>()
    val profileEditSuccess: LiveData<Boolean> get() = _profileEditSuccess

    fun getUserProfile() = userInfoRepository.getMemberProfileUrl()

    fun posting(contentText: String) =
        viewModelScope.launch {
            postingRepository.posting(contentText).collectLatest {
                _postPosting.value = UiState.Success(it)
            }
            _postPosting.value = UiState.Loading
        }

    fun setIntroduction(input: String) {
        this._introduction.value = input
    }

    fun patchUserProfileEdit(
        nickName: String,
        allowed: Boolean?,
        intro: String,
        url: String?,
    ) {
        viewModelScope.launch {
            loginRepository.patchProfileEdit(
                nickName,
                allowed,
                intro,
                url,
            ).collectLatest {
                _profileEditSuccess.value = it
            }
        }
    }

    fun getNickName() = userInfoRepository.getNickName()

    fun getIsNewUser() = userInfoRepository.getIsNewUser()

    fun saveCheckLogin(checkLogin: Boolean) = userInfoRepository.saveCheckLogin(checkLogin)

    fun getCheckOnboarding() = userInfoRepository.getCheckOnboarding()

    fun saveCheckOnboarding(checkOnboarding: Boolean) =
        userInfoRepository.saveCheckOnboarding(checkOnboarding)
}
