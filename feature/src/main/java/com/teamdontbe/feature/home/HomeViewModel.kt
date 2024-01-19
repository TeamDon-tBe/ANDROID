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
import kotlinx.coroutines.sync.Mutex
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

        private val _getFeedDetail = MutableStateFlow<UiState<FeedEntity>>(UiState.Empty)
        val getFeedDetail: StateFlow<UiState<FeedEntity>> = _getFeedDetail

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

        fun getFeedList() =
            viewModelScope.launch {
                homeRepository.getFeedList().collectLatest {
                    if (it != null) _getFeedList.value = UiState.Success(it) else UiState.Empty
                }
                _getFeedList.value = UiState.Loading
            }

        fun getFeedDetail(contentId: Int) =
            viewModelScope.launch {
                homeRepository.getFeedLDetail(contentId).collectLatest {
                    if (it != null) _getFeedDetail.value = UiState.Success(it) else UiState.Empty
                }
                _getFeedDetail.value = UiState.Loading
            }

        fun getCommentList(contentId: Int) =
            viewModelScope.launch {
                homeRepository.getCommentList(contentId).collectLatest {
                    if (it != null) _getCommentList.value = UiState.Success(it) else UiState.Empty
                }
                _getCommentList.value = UiState.Loading
            }

        fun deleteFeed(contentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteFeed(contentId).collectLatest {
                    _deleteFeed.emit(UiState.Success(it))
                }
                _deleteFeed.emit(UiState.Loading)
            }

        fun getMemberId() = userInfoRepository.getMemberId()

        fun getUserNickname() = userInfoRepository.getNickName()

        private val likeMutex = Mutex()

        fun postFeedLiked(contentId: Int) =
            viewModelScope.launch {
                likeMutex.tryLock(1000)
                try {
                    homeRepository.postFeedLiked(contentId).collectLatest {
                        _postFeedLiked.emit(UiState.Success(it))
                    }
                } finally {
                    likeMutex.unlock()
                }
                _postFeedLiked.emit(UiState.Loading)
            }

        fun deleteFeedLiked(contentId: Int) =
            viewModelScope.launch {
                likeMutex.tryLock(1000)
                try {
                    if (likeMutex.isLocked) {
                        _deleteFeedLiked.emit(UiState.Failure("막힘"))
                    } else {
                        homeRepository.deleteFeedLiked(contentId).collectLatest {
                            _deleteFeedLiked.emit(UiState.Success(it))
                        }
                    }
                } finally {
                    likeMutex.unlock()
                }
                _deleteFeedLiked.emit(UiState.Loading)
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
                if (likeMutex.tryLock(1000)) {
                    try {
                        homeRepository.postCommentLiked(commentId).collectLatest {
                        }
                    } finally {
                        likeMutex.unlock()
                    }
                }
            }

        fun deleteCommentLiked(commentId: Int) =
            viewModelScope.launch {
                if (likeMutex.tryLock(1000)) {
                    try {
                        homeRepository.deleteCommentLiked(commentId).collectLatest {
                        }
                    } finally {
                        likeMutex.unlock()
                    }
                }
            }

        fun postTransparent(
            targetMemberId: Int,
            alarmTriggerId: Int,
        ) = viewModelScope.launch {
            homeRepository.postTransparent(targetMemberId, alarmTriggerId).collectLatest {
                _postTransparent.emit(UiState.Success(it))
            }
            _postTransparent.emit(UiState.Loading)
        }
    }
