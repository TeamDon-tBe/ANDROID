package com.teamdontbe.feature.posting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.repository.PostingRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel
    @Inject
    constructor(
        private val postingRepository: PostingRepository,
        private val userInfoRepository: UserInfoRepository,
    ) : ViewModel() {
        private val _postPosting = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val postPosting: StateFlow<UiState<Boolean>> = _postPosting

        fun posting(contentText: String) =
            viewModelScope.launch {
                postingRepository.posting(contentText).collectLatest {
                    _postPosting.value = UiState.Success(it)
                }
                _postPosting.value = UiState.Loading
            }

        fun getNickName() = userInfoRepository.getNickName()

        fun getMemberProfileUrl() = userInfoRepository.getMemberProfileUrl()
    }
