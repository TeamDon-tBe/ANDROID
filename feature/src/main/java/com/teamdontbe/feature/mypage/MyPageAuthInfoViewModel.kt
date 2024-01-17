package com.teamdontbe.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserAccountInfoEntity
import com.teamdontbe.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageAuthInfoViewModel
@Inject constructor(private val myPageRepository: MyPageRepository) :
    ViewModel() {

    private val _getMyPageUserAccountInfoListState =
        MutableStateFlow<UiState<MyPageUserAccountInfoEntity>>(UiState.Empty)
    val getMyPageUserAccountInfoState: StateFlow<UiState<MyPageUserAccountInfoEntity>> =
        _getMyPageUserAccountInfoListState

    fun getMyPageFeedList() {
        viewModelScope.launch {
            myPageRepository.getMyPageUserAccountInfo().collectLatest {
                if (it != null) {
                    _getMyPageUserAccountInfoListState.value =
                        UiState.Success(it)
                } else {
                    UiState.Empty
                }
            }
            _getMyPageUserAccountInfoListState.value = UiState.Loading
        }
    }
}
