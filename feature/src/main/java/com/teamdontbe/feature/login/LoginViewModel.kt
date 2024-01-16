package com.teamdontbe.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.AuthEntity
import com.teamdontbe.domain.repository.AuthRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val userInfoRepository: UserInfoRepository,
    ) : ViewModel() {
        private val _postLogin = MutableStateFlow<UiState<AuthEntity>>(UiState.Empty)
        val postLogin: StateFlow<UiState<AuthEntity>> = _postLogin

        fun login(socialType: String) =
            viewModelScope.launch {
                authRepository.login(socialType).collectLatest {
                    if (it != null) _postLogin.value = UiState.Success(it) else UiState.Empty
                }
                _postLogin.value = UiState.Loading
            }

        fun getAccessToken() = userInfoRepository.getAccessToken()

        fun saveAccessToken(accessToken: String) = userInfoRepository.saveAccessToken(accessToken)

        fun getRefreshToken() = userInfoRepository.getRefreshToken()

        fun saveRefreshToken(refreshToken: String) = userInfoRepository.saveAccessToken(refreshToken)

        fun getMemberId() = userInfoRepository.getMemberId()

        fun saveMemberId(memberId: Int) = userInfoRepository.saveMemberId(memberId)

        fun getNickName() = userInfoRepository.getNickName()

        fun saveNickName(nickName: String) = userInfoRepository.saveNickName(nickName)

        fun checkLogin() = userInfoRepository.checkLogin()

        fun saveCheckLogin(checkLogin: Boolean) = userInfoRepository.saveCheckLogin(checkLogin)
    }
