package com.teamdontbe.feature.mypage.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageCommentEntity
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
class MyPageCommentViewModel
    @Inject
    constructor(
        private val myPageRepository: MyPageRepository,
        private val homeRepository: HomeRepository,
        private val userInfoRepository: UserInfoRepository,
    ) :
    ViewModel() {
        private val _getMyPageCommentListState =
            MutableStateFlow<UiState<List<MyPageCommentEntity>>>(UiState.Empty)
        val getMyPageCommentListState: StateFlow<UiState<List<MyPageCommentEntity>>> =
            _getMyPageCommentListState

        private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
        val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

        private val _deleteComment = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val deleteComment: StateFlow<UiState<Boolean>> get() = _deleteComment

        fun getMyPageCommentList(viewMemberId: Int) {
            viewModelScope.launch {
                myPageRepository.getMyPageCommentList(viewMemberId).collectLatest {
                    if (it != null) {
                        _getMyPageCommentListState.value =
                            UiState.Success(it)
                    } else {
                        UiState.Empty
                    }
                }
                _getMyPageCommentListState.value = UiState.Loading
            }
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

        fun deleteComment(commentId: Int) =
            viewModelScope.launch {
                homeRepository.deleteComment(commentId).collectLatest {
                    _deleteComment.value = UiState.Success(it)
                }
                _deleteComment.value = UiState.Loading
            }

        fun getMemberId() = userInfoRepository.getMemberId()
    }
