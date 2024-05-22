package com.teamdontbe.feature.posting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.PostingRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _postPosting = MutableSharedFlow<UiState<Boolean>>()
    val postPosting: SharedFlow<UiState<Boolean>> get() = _postPosting.asSharedFlow()

    private val _getMyPageUserProfileState =
        MutableStateFlow<UiState<MyPageUserProfileEntity>>(UiState.Empty)
    val getMyPageUserProfileState: StateFlow<UiState<MyPageUserProfileEntity>> =
        _getMyPageUserProfileState

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    fun setPhotoUri(uri: String?) {
        _photoUri.value = uri
    }

    fun posting(contentText: String, imageString: String?) =
        viewModelScope.launch {
            _postPosting.emit(UiState.Loading)
            postingRepository.postingMultiPart(contentText, imageString)
                .onSuccess { isSuccess ->
                    if (isSuccess) _postPosting.emit(UiState.Success(isSuccess))
                    else _postPosting.emit(UiState.Failure("포스팅 실패"))
                }.onFailure {
                    _postPosting.emit(UiState.Failure(it.message.orEmpty()))
                }
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
