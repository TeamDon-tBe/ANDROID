package com.teamdontbe.feature.mypage.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
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
class MyPageFeedViewModel
    @Inject
    constructor(
        private val myPageRepository: MyPageRepository,
        private val homeRepository: HomeRepository,
        private val userInfoRepository: UserInfoRepository,
    ) :
    ViewModel() {
        private val _getMyPageFeedListState =
            MutableStateFlow<UiState<List<FeedEntity>>>(UiState.Empty)
        val getMyPageFeedListState: StateFlow<UiState<List<FeedEntity>>> =
            _getMyPageFeedListState

        private val _deleteFeed = MutableSharedFlow<UiState<Boolean>>()
        val deleteFeed: SharedFlow<UiState<Boolean>> = _deleteFeed

        private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
        val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

        fun getMyPageFeedList(viewMemberId: Int) {
            viewModelScope.launch {
                myPageRepository.getMyPageFeedList(viewMemberId).collectLatest {
                    if (it != null) {
                        _getMyPageFeedListState.value =
                            UiState.Success(it)
                    } else {
                        UiState.Empty
                    }
                }
                _getMyPageFeedListState.value = UiState.Loading
            }
        }

        fun postFeedLiked(commentId: Int) =
            viewModelScope.launch {
                homeRepository.postFeedLiked(commentId).collectLatest {
                }
            }

        fun deleteFeedLiked(commentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteFeedLiked(commentId).collectLatest {
                }
            }

        fun deleteFeed(contentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteFeed(contentId).collectLatest {
                    _deleteFeed.emit(UiState.Success(it))
                }
                _deleteFeed.emit(UiState.Loading)
            }

        fun getMemberId() = userInfoRepository.getMemberId()

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
