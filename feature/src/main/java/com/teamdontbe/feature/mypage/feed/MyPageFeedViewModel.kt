package com.teamdontbe.feature.mypage.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageFeedViewModel
@Inject constructor(private val myPageUserProfileRepository: MyPageRepository) :
    ViewModel() {
    private val _getMyPageFeedListState =
        MutableStateFlow<UiState<List<FeedEntity>>>(UiState.Empty)
    val getMyPageFeedListState: StateFlow<UiState<List<FeedEntity>>> =
        _getMyPageFeedListState

    fun getMyPageUserProfileInfo(viewMemberId: Int) {
        viewModelScope.launch {
            myPageUserProfileRepository.getMyPageFeedList(viewMemberId).collectLatest {
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
}
