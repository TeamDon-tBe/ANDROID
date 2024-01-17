package com.teamdontbe.feature.mypage.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageCommentViewModel
@Inject constructor(private val myPageRepository: MyPageRepository) :
    ViewModel() {
    private val _getMyPageCommentListState =
        MutableStateFlow<UiState<List<MyPageCommentEntity>>>(UiState.Empty)
    val getMyPageCommentListState: StateFlow<UiState<List<MyPageCommentEntity>>> =
        _getMyPageCommentListState

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
}
