package com.teamdontbe.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import com.teamdontbe.feature.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val homeRepository: HomeRepository,
        private val userInfoRepository: UserInfoRepository,
    ) : ViewModel() {
        private val _getFeedList = MutableStateFlow<UiState<List<FeedEntity>>>(UiState.Empty)
        val getFeedList: StateFlow<UiState<List<FeedEntity>>> = _getFeedList

        private val _getFeedDetail = MutableSharedFlow<UiState<FeedEntity>>()
        val getFeedDetail: SharedFlow<UiState<FeedEntity>> = _getFeedDetail

        private val _getCommentList = MutableStateFlow<UiState<List<CommentEntity>>>(UiState.Empty)
        val getCommentList: StateFlow<UiState<List<CommentEntity>>> = _getCommentList

        private val _deleteFeed = MutableSharedFlow<UiState<Boolean>>()
        val deleteFeed: SharedFlow<UiState<Boolean>> = _deleteFeed

        private val _postFeedLiked = MutableSharedFlow<UiState<Boolean>>()
        val postFeedLiked: SharedFlow<UiState<Boolean>> get() = _postFeedLiked

        private val _deleteFeedLiked = MutableSharedFlow<UiState<Boolean>>()
        val deleteFeedLiked: SharedFlow<UiState<Boolean>> get() = _deleteFeedLiked

        private val _postCommentPosting = MutableLiveData<Event<Boolean>>()
        val postCommentPosting: LiveData<Event<Boolean>> get() = _postCommentPosting

        private val _deleteComment = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val deleteComment: StateFlow<UiState<Boolean>> get() = _deleteComment

        private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
        val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

        private val _openHomeDetail = MutableLiveData<Event<FeedEntity>>()
        val openHomeDetail: LiveData<Event<FeedEntity>> = _openHomeDetail

        fun openHomeDetail(feedEntity: FeedEntity) {
            _openHomeDetail.value = Event(feedEntity)
        }

        fun getFeedList() =
            viewModelScope.launch {
                _getFeedList.value = UiState.Loading
                homeRepository.getFeedList().collectLatest {
                    if (it != null) _getFeedList.value = UiState.Success(it) else UiState.Failure("null")
                }
            }

        fun getFeedDetail(contentId: Int) =
            viewModelScope.launch {
                _getFeedDetail.emit(UiState.Loading)
                homeRepository.getFeedLDetail(contentId).collectLatest {
                    if(it!=null) _getFeedDetail.emit(UiState.Success(it)) else UiState.Failure("null")
                }
            }

        fun getCommentList(contentId: Int) =
            viewModelScope.launch {
                _getCommentList.value = UiState.Loading
                homeRepository.getCommentList(contentId).collectLatest {
                    if (it != null) _getCommentList.value = UiState.Success(it) else UiState.Failure("null")
                }
            }

        fun deleteFeed(contentId: Int) =
            viewModelScope.launch {
                _deleteFeed.emit(UiState.Loading)
                homeRepository.deleteFeed(contentId).collectLatest {
                    _deleteFeed.emit(UiState.Success(it))
                }
            }

        fun getMemberId() = userInfoRepository.getMemberId()

        fun getUserNickname() = userInfoRepository.getNickName()

        fun postFeedLiked(contentId: Int) =
            viewModelScope.launch {
                homeRepository.postFeedLiked(contentId).collectLatest {
                    _postFeedLiked.emit(UiState.Success(it))
                }
            }

        fun deleteFeedLiked(contentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteFeedLiked(contentId).collectLatest {
                    _deleteFeedLiked.emit(UiState.Success(it))
                }
            }

        fun postCommentPosting(
            contentId: Int,
            commentText: String,
        ) = viewModelScope.launch {
            homeRepository.postCommentPosting(contentId, commentText).collectLatest {
                _postCommentPosting.value = Event(it)
            }
            _postCommentPosting.value = Event(false)
        }

        fun deleteComment(commentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteComment(commentId).collectLatest {
                    _deleteComment.value = UiState.Success(it)
                }
                _deleteComment.value = UiState.Loading
            }

        fun postCommentLiked(commentId: Int) =
            viewModelScope.launch {
                homeRepository.postCommentLiked(commentId).collectLatest {
                }
            }

        fun deleteCommentLiked(commentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteCommentLiked(commentId).collectLatest {
                }
            }

        fun postTransparent(
            alarmTriggerType: String,
            targetMemberId: Int,
            alarmTriggerId: Int,
        ) = viewModelScope.launch {
            _postTransparent.emit(UiState.Loading)
            homeRepository.postTransparent(alarmTriggerType, targetMemberId, alarmTriggerId).collectLatest {
                _postTransparent.emit(UiState.Success(it))
            }
        }
    }
