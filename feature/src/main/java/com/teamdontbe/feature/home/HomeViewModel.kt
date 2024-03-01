package com.teamdontbe.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import com.teamdontbe.feature.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val homeRepository: HomeRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {

    private val _getFeedDetail = MutableSharedFlow<UiState<FeedEntity>>()
    val getFeedDetail: SharedFlow<UiState<FeedEntity>> = _getFeedDetail

    private val _deleteFeed = MutableSharedFlow<UiState<Boolean>>()
    val deleteFeed: SharedFlow<UiState<Boolean>> = _deleteFeed

    private val _postFeedLiked = MutableSharedFlow<UiState<Boolean>>()
    val postFeedLiked: SharedFlow<UiState<Boolean>> get() = _postFeedLiked

    private val _deleteFeedLiked = MutableSharedFlow<UiState<Boolean>>()
    val deleteFeedLiked: SharedFlow<UiState<Boolean>> get() = _deleteFeedLiked

    private val _postCommentPosting = MutableSharedFlow<UiState<Boolean>>()
    val postCommentPosting: SharedFlow<UiState<Boolean>> get() = _postCommentPosting

    private val _deleteComment = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteComment: StateFlow<UiState<Boolean>> get() = _deleteComment

    private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
    val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

    private val _openHomeDetail = MutableLiveData<Event<FeedEntity>>()
    val openHomeDetail: LiveData<Event<FeedEntity>> = _openHomeDetail

    fun openHomeDetail(feedEntity: FeedEntity) {
        _openHomeDetail.value = Event(feedEntity)
    }

    fun getFeedList() = homeRepository.getFeedList()

    fun getFeedDetail(contentId: Int) = viewModelScope.launch {
        _getFeedDetail.emit(UiState.Loading)
        homeRepository.getFeedLDetail(contentId)
            .fold(
                { if (it != null) _getFeedDetail.emit(UiState.Success(it)) else UiState.Failure("null") },
                { UiState.Failure("null") }
            )
    }

    fun getCommentList(contentId: Int) = homeRepository.getCommentList(contentId)

    fun deleteFeed(contentId: Int) = viewModelScope.launch {
        _deleteFeed.emit(UiState.Loading)
        homeRepository.deleteFeed(contentId)
            .fold(
                { if (it) _deleteFeed.emit(UiState.Success(true)) else UiState.Failure("null") },
                {}
            )
    }

    fun getMemberId() = userInfoRepository.getMemberId()

    fun getUserNickname() = userInfoRepository.getNickName()

    fun getUserProfile() = userInfoRepository.getMemberProfileUrl()

    fun postFeedLiked(contentId: Int) = viewModelScope.launch {
        homeRepository.postFeedLiked(contentId)
            .fold({ _postFeedLiked.emit(UiState.Success(it)) }, {})
    }

    fun deleteFeedLiked(contentId: Int) = viewModelScope.launch {
        homeRepository.deleteFeedLiked(contentId)
            .fold({ _deleteFeedLiked.emit(UiState.Success(it)) }, {})
    }

    fun postCommentPosting(
        contentId: Int,
        commentText: String,
    ) = viewModelScope.launch {
        homeRepository.postCommentPosting(contentId, commentText)
            .fold(
                { if (it) _deleteFeed.emit(UiState.Success(it)) else UiState.Failure("null") },
                {}
            )
    }

    fun deleteComment(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteComment(commentId)
            .fold(
                { if (it) _deleteFeed.emit(UiState.Success(it)) else UiState.Failure("null") },
                {}
            )
        _deleteComment.value = UiState.Loading
    }

    fun postCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.postCommentLiked(commentId).fold({}, {})
    }

    fun deleteCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteCommentLiked(commentId).fold({}, {})
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
            if (it) _postTransparent.emit(UiState.Success(true)) else _postTransparent.emit(
                UiState.Failure(
                    "400"
                )
            )
        }, { Timber.d("500") })
    }
}
