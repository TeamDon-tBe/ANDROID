package com.teamdontbe.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.AuthEntity
import com.teamdontbe.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _postLogin = MutableStateFlow<UiState<AuthEntity?>>(UiState.Empty)
    val postLogin: StateFlow<UiState<AuthEntity?>> = _postLogin

    fun login(auth: String, socialType: String) = viewModelScope.launch {
        authRepository.login(auth, socialType).collectLatest {
            _postLogin.value = UiState.Success(it)
        }
        _postLogin.value = UiState.Loading
    }
}