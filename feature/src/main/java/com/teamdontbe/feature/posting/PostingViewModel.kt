package com.teamdontbe.feature.posting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.PostingRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel
@Inject
constructor(
    private val postingRepository: PostingRepository,
    private val userInfoRepository: UserInfoRepository,
    private val myPageRepository: MyPageRepository,
) : ViewModel() {
    private val _postPosting = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val postPosting: StateFlow<UiState<Boolean>> = _postPosting

    private val _getMyPageUserProfileState =
        MutableStateFlow<UiState<MyPageUserProfileEntity>>(UiState.Empty)
    val getMyPageUserProfileState: StateFlow<UiState<MyPageUserProfileEntity>> =
        _getMyPageUserProfileState

    fun posting(contentText: String) =
        viewModelScope.launch {
            postingRepository.posting(contentText).collectLatest {
                _postPosting.value = UiState.Success(it)
            }
            _postPosting.value = UiState.Loading
        }

    fun getNickName() = userInfoRepository.getNickName()

    fun getMemberId() = userInfoRepository.getMemberId()

    fun getMemberProfileUrl() = userInfoRepository.getMemberProfileUrl()

    fun getMyPageUserProfileInfo(viewMemberId: Int) =
        viewModelScope.launch {
            myPageRepository.getMyPageUserProfile(viewMemberId).onSuccess {
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
