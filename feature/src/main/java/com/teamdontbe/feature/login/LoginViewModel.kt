package com.teamdontbe.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.LoginEntity
import com.teamdontbe.domain.repository.LoginRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val loginRepository: LoginRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _postLogin = MutableStateFlow<UiState<LoginEntity>>(UiState.Empty)
    val postLogin: StateFlow<UiState<LoginEntity>> = _postLogin

    fun postLogin(socialType: String) = viewModelScope.launch {
        _postLogin.value = UiState.Loading
        loginRepository.postLogin(socialType).fold({
            _postLogin.value = if (it != null) UiState.Success(it) else UiState.Failure("400")
        }, {
            _postLogin.value = UiState.Failure(it.message.toString())
        })
    }

    fun getAccessToken() = userInfoRepository.getAccessToken()

    fun saveAccessToken(accessToken: String) = userInfoRepository.saveAccessToken(accessToken)

    fun getRefreshToken() = userInfoRepository.getRefreshToken()

    fun saveRefreshToken(refreshToken: String) = userInfoRepository.saveRefreshToken(refreshToken)

    fun getMemberId() = userInfoRepository.getMemberId()

    fun saveMemberId(memberId: Int) = userInfoRepository.saveMemberId(memberId)

    fun getNickName() = userInfoRepository.getNickName()

    fun saveNickName(nickName: String) = userInfoRepository.saveNickName(nickName)

    fun checkLogin() = userInfoRepository.checkLogin()

    fun saveCheckLogin(checkLogin: Boolean) = userInfoRepository.saveCheckLogin(checkLogin)

    fun getMemberProfileUrl() = userInfoRepository.getMemberProfileUrl()

    fun saveMemberProfileUrl(memberUrl: String) = userInfoRepository.saveMemberProfileUrl(memberUrl)

    fun getIsNewUser() = userInfoRepository.getIsNewUser()

    fun saveIsNewUser(isNewUser: Boolean) = userInfoRepository.saveIsNewUser(isNewUser)
}
