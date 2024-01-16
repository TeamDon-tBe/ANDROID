package com.teamdontbe.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
    @Inject
    constructor(
        private val notificationRepository: NotificationRepository,
    ) : ViewModel() {
        init {
            getNotificationCount()
            getNotificationList()
        }

        private val _getNotiCount = MutableStateFlow<UiState<Int>>(UiState.Empty)
        val getNotiCount: StateFlow<UiState<Int>> = _getNotiCount

        private val _getNotiList = MutableStateFlow<UiState<List<NotiEntity>>>(UiState.Empty)
        val getNotiList: StateFlow<UiState<List<NotiEntity>>> = _getNotiList

        private fun getNotificationCount() =
            viewModelScope.launch {
                notificationRepository.getNotificationCount().collectLatest {
                    if (it != null) _getNotiCount.value = UiState.Success(it) else UiState.Empty
                }
                _getNotiCount.value = UiState.Loading
            }

        private fun getNotificationList() =
            viewModelScope.launch {
                notificationRepository.getNotificationList().collectLatest {
                    if (it != null) _getNotiList.value = UiState.Success(it) else UiState.Empty
                }
                _getNotiList.value = UiState.Loading
            }
    }
