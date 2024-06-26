package com.teamdontbe.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.HomeRepository
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
@Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val userInfoRepository: UserInfoRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _getMyPageUserProfileState =
        MutableStateFlow<UiState<MyPageUserProfileEntity>>(UiState.Empty)
    val getMyPageUserProfileState: StateFlow<UiState<MyPageUserProfileEntity>> =
        _getMyPageUserProfileState

    private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
    val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

    private val _imageUrl = MutableStateFlow<String>("")

    fun getMemberId() = userInfoRepository.getMemberId()

    fun getUserImgUrl() = userInfoRepository.getMemberProfileUrl()

    fun getMyPageUserProfileInfo(viewMemberId: Int) {
        viewModelScope.launch {
            _getMyPageUserProfileState.value = UiState.Loading
            myPageRepository.getMyPageUserProfile(viewMemberId).onSuccess {
                if (it != null) {
                    _getMyPageUserProfileState.value = UiState.Success(it)
                } else {
                    UiState.Failure("null")
                }
            }
        }
    }

    // 이미지 변화 감지
    fun updateImageUrl(newUrl: String) {
        if (_imageUrl.value != newUrl) {
            _imageUrl.value = newUrl
            viewModelScope.launch {
                saveUserProfileUriLocal(newUrl)
            }
        }
    }

    // 이미지 url 로컬 저장
    private fun saveUserProfileUriLocal(uri: String) {
        userInfoRepository.saveMemberProfileUrl(uri)
    }

    //    feed
    private val _deleteFeed = MutableSharedFlow<UiState<Boolean>>()
    val deleteFeed: SharedFlow<UiState<Boolean>> = _deleteFeed
    fun getMyPageFeedList(viewMemberId: Int) = myPageRepository.getMyPageFeedList(viewMemberId)

    fun postFeedLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.postFeedLiked(commentId).fold({}, {})
    }

    fun deleteFeedLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteFeedLiked(commentId).fold({}, {})
    }

    fun deleteFeed(contentId: Int) = viewModelScope.launch {
        _deleteFeed.emit(UiState.Loading)
        homeRepository.deleteFeed(contentId).fold(
            { _deleteFeed.emit(UiState.Success(true)) },
            { _deleteFeed.emit(UiState.Failure(it.message.toString())) }
        )
    }

    //    comment
    private val _deleteComment = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteComment: StateFlow<UiState<Boolean>> get() = _deleteComment

    fun getMyPageCommentList(viewMemberId: Int) =
        myPageRepository.getMyPageCommentList(viewMemberId)

    fun postCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.postCommentLiked(commentId).fold({}, {})
    }

    fun deleteCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteCommentLiked(commentId).fold({}, {})
    }

    fun deleteComment(commentId: Int) = viewModelScope.launch {
        _deleteComment.emit(UiState.Loading)
        homeRepository.deleteComment(commentId).fold(
            { _deleteComment.emit(UiState.Success(it)) },
            { _deleteComment.emit(UiState.Failure(it.message.toString())) }
        )
    }

    fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ) = viewModelScope.launch {
        _postTransparent.emit(UiState.Loading)
        homeRepository.postTransparent(
            alarmTriggerType,
            targetMemberId,
            alarmTriggerId,
            ghostReason
        ).fold({
            _postTransparent.emit(UiState.Success(true))
        }, { _postTransparent.emit(UiState.Failure(it.message.toString())) })
    }

    private val _postComplaint = MutableSharedFlow<Boolean>()
    val postComplaint: SharedFlow<Boolean> get() = _postComplaint

    fun postComplaint(
        reportTargetNickname: String,
        relateText: String
    ) {
        viewModelScope.launch {
            homeRepository.postComplaint(
                reportTargetNickname,
                relateText
            ).fold({ _postComplaint.emit(true) }, { _postComplaint.emit(false) })
        }
    }
}
