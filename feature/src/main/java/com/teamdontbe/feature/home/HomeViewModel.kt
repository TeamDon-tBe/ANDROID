package com.teamdontbe.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val homeRepository: HomeRepository,
    ) : ViewModel() {
        private val _getFeedList = MutableStateFlow<UiState<List<FeedEntity>>>(UiState.Empty)
        val getFeedList: StateFlow<UiState<List<FeedEntity>>> = _getFeedList

        private val _getFeedDetail = MutableStateFlow<UiState<FeedEntity>>(UiState.Empty)
        val getFeedDetail: StateFlow<UiState<FeedEntity>> = _getFeedDetail

        private val _getCommentList = MutableStateFlow<UiState<List<CommentEntity>>>(UiState.Empty)
        val getCommentList: StateFlow<UiState<List<CommentEntity>>> = _getCommentList

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
    }
