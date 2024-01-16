package com.teamdontbe.feature.posting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.repository.PostingRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
        private val _postPosting = MutableSharedFlow<UiState<Boolean>>()
        val postPosting: SharedFlow<UiState<Boolean>> = _postPosting

        private val _introduction = MutableLiveData<String>()
        val introduction: LiveData<String> get() = _introduction

        fun posting(contentText: String) =
            viewModelScope.launch {
                postingRepository.posting(contentText).collectLatest {
                    _postPosting.emit(UiState.Success(it))
                }
                _postPosting.emit(UiState.Loading)
            }

        fun getNickName() = userInfoRepository.getNickName()

        fun setIntroduction(input: String) {
            this._introduction.value = input
        }
    }
