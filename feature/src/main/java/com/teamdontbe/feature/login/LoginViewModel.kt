package com.teamdontbe.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.LoginEntity
import com.teamdontbe.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _postLogin = MutableStateFlow<UiState<LoginEntity?>>(UiState.Empty)
    val postLogin: StateFlow<UiState<LoginEntity?>> = _postLogin

    fun login(auth: String, socialType: String) = viewModelScope.launch {
        loginRepository.login(auth, socialType).collectLatest {
            if (it != null) {
                _postLogin.value = UiState.Success(it)
            }else{
                _postLogin.value = UiState.Empty
            }
        }
        _postLogin.value = UiState.Loading
    }
}