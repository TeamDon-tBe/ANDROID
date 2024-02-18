package com.teamdontbe.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.HomeRepository
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    fun getMemberId() = userInfoRepository.getMemberId()

    fun checkLogin(check: Boolean) = userInfoRepository.saveCheckLogin(check)

    private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
    val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

    fun getMyPageUserProfileInfo(viewMemberId: Int) {
        viewModelScope.launch {
            myPageRepository.getMyPageUserProfile(viewMemberId).collectLatest {
                if (it != null) {
                    _getMyPageUserProfileState.value = UiState.Success(it)
                } else {
                    UiState.Empty
                }
            }
            _getMyPageUserProfileState.value = UiState.Loading
        }
    }

    //    feed
    private val _getMyPageFeedListState = MutableStateFlow<UiState<List<FeedEntity>>>(UiState.Empty)
    val getMyPageFeedListState: StateFlow<UiState<List<FeedEntity>>> = _getMyPageFeedListState

    private val _deleteFeed = MutableSharedFlow<UiState<Boolean>>()
    val deleteFeed: SharedFlow<UiState<Boolean>> = _deleteFeed
    fun getMyPageFeedList(viewMemberId: Int) {
        viewModelScope.launch {
            myPageRepository.getMyPageFeedList(viewMemberId).collectLatest {
                if (it != null) {
                    _getMyPageFeedListState.value = UiState.Success(it)
                } else {
                    UiState.Empty
                }
            }
            _getMyPageFeedListState.value = UiState.Loading
        }
    }

    fun postFeedLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.postFeedLiked(commentId).collectLatest {}
    }

    fun deleteFeedLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteFeedLiked(commentId).collectLatest {}
    }

    fun deleteFeed(contentId: Int) =
        viewModelScope.launch {
            homeRepository.deleteFeed(contentId).collectLatest {
                _deleteFeed.emit(UiState.Success(it))
            }
            _deleteFeed.emit(UiState.Loading)
        }

    //    comment
    private val _getMyPageCommentListState =
        MutableStateFlow<UiState<List<MyPageCommentEntity>>>(UiState.Empty)
    val getMyPageCommentListState: StateFlow<UiState<List<MyPageCommentEntity>>> =
        _getMyPageCommentListState

    private val _deleteComment = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteComment: StateFlow<UiState<Boolean>> get() = _deleteComment

    fun getMyPageCommentList(viewMemberId: Int) {
        viewModelScope.launch {
            myPageRepository.getMyPageCommentList(viewMemberId).collectLatest {
                if (it != null) {
                    _getMyPageCommentListState.value = UiState.Success(it)
                } else {
                    UiState.Empty
                }
            }
            _getMyPageCommentListState.value = UiState.Loading
        }
    }

    fun postCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.postCommentLiked(commentId).collectLatest {}
    }

    fun deleteCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteCommentLiked(commentId).collectLatest {}
    }

    fun deleteComment(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteComment(commentId).collectLatest {
            _deleteComment.value = UiState.Success(it)
        }
        _deleteComment.value = UiState.Loading
    }

    fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) = viewModelScope.launch {
        homeRepository.postTransparent(alarmTriggerType, targetMemberId, alarmTriggerId).collectLatest {
            _postTransparent.emit(UiState.Success(it))
        }
        _postTransparent.emit(UiState.Loading)
    }
}
