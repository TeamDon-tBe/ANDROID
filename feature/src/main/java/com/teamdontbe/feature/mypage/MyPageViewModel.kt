package com.teamdontbe.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
@Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val userInfoRepository: UserInfoRepository,

) : ViewModel() {
    private val _getMyPageUserProfileState =
        MutableStateFlow<UiState<MyPageUserProfileEntity>>(UiState.Empty)
    val getMyPageUserProfileState: StateFlow<UiState<MyPageUserProfileEntity>> =
        _getMyPageUserProfileState

    fun getMemberId() = userInfoRepository.getMemberId()

    fun getMyPageUserProfileInfo(viewMemberId: Int) {
        viewModelScope.launch {
            myPageRepository.getMyPageUserProfile(viewMemberId).collectLatest {
                if (it != null) {
                    _getMyPageUserProfileState.value =
                        UiState.Success(it)
                } else {
                    UiState.Empty
                }
            }
            _getMyPageUserProfileState.value = UiState.Loading
        }
    }
}
