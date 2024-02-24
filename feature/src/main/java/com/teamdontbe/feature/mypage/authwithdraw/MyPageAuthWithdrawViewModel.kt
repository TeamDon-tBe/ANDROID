package com.teamdontbe.feature.mypage.authwithdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.repository.UserInfoRepository
import com.teamdontbe.domain.repository.WithdrawRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageAuthWithdrawViewModel
@Inject
constructor(
    private val withdrawRepository: WithdrawRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _deleteWithdraw = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
    val deleteWithdraw: StateFlow<UiState<Boolean>> = _deleteWithdraw

    fun deleteWithdraw(reason: String) =
        viewModelScope.launch {
            withdrawRepository.deleteWithdraw(reason).collectLatest {
                _deleteWithdraw.value = UiState.Success(it)
            }
            _deleteWithdraw.value = UiState.Loading
        }

    fun getNickName() = userInfoRepository.getNickName()

    fun checkLogin(check: Boolean) = userInfoRepository.saveCheckLogin(check)
}