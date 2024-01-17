package com.teamdontbe.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
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

        private val _openComplaintDialog = MutableSharedFlow<UiState<Int>>()
        val openComplaintDialog: SharedFlow<UiState<Int>> = _openComplaintDialog

        private val _openDeleteDialog = MutableSharedFlow<UiState<Int>>()
        val openDeleteDialog: SharedFlow<UiState<Int>> = _openComplaintDialog

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
                _getFeedList.value = UiState.Loading
            }

        fun getCommentList(contentId: Int) =
            viewModelScope.launch {
                homeRepository.getCommentList(contentId).collectLatest {
                    if (it != null) _getCommentList.value = UiState.Success(it) else UiState.Empty
                }
                _getFeedList.value = UiState.Loading
            }

        fun deleteFeed(contentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteFeed(contentId).collectLatest {
                    _deleteFeed.emit(UiState.Success(it))
                }
                _getFeedList.value = UiState.Loading
            }

        fun getMemberId() = userInfoRepository.getMemberId()

        fun openComplaintDialog(contentId: Int) {
            Timber.tag("ttt").d("찍힘")
            viewModelScope.launch {
                _openComplaintDialog.emit(UiState.Success(contentId))
                Timber.tag("ttt").d(contentId.toString())
            }
        }

        fun openDeleteDialog(contentId: Int) {
            Timber.tag("ttt").d("찍힘")
            viewModelScope.launch {
                _openDeleteDialog.emit(UiState.Success(contentId))
            }
        }
    }
