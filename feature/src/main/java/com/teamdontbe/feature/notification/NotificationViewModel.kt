package com.teamdontbe.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.domain.repository.NotificationListRepository
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
        private val notificationListRepository: NotificationListRepository,
    ) : ViewModel() {
        init {
            getNotificationList()
        }

        private val _getNotiList = MutableStateFlow<UiState<List<NotiEntity>>>(UiState.Empty)
        val getNotiList: StateFlow<UiState<List<NotiEntity>>> = _getNotiList

        fun getNotificationList() =
            viewModelScope.launch {
                notificationListRepository.getNotificationList().collectLatest {
                    if (it != null) _getNotiList.value = UiState.Success(it) else UiState.Empty
                }
                _getNotiList.value = UiState.Loading
            }
    }
