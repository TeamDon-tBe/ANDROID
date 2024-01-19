package com.teamdontbe.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.repository.LoginRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpProfileViewModel
@Inject constructor(
    private val loginRepository: LoginRepository,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private var _isBtnSelected = MutableLiveData<Boolean>()
    val isBtnSelected: LiveData<Boolean> get() = _isBtnSelected

    private var _isNickNameValid = MutableLiveData<Boolean>()
    val isNickNameValid: LiveData<Boolean> get() = _isNickNameValid

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> get() = _nickName

    private var _introduceNum = MutableLiveData<String>()
    val introduceNum = _introduceNum

    private val _nickNameDoubleState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val nickNameDoubleState: StateFlow<UiState<String>> = _nickNameDoubleState

    private val _profileEditSuccess = MutableLiveData<Boolean>()
    val profileEditSuccess: LiveData<Boolean> get() = _profileEditSuccess

    init {
        _isBtnSelected.value = false
    }

    fun setUserNickName(nickName: String) {
        userInfoRepository.saveNickName(nickName)
    }

    fun patchUserProfileEdit(nickName: String, allowed: Boolean, intro: String, url: String?) {
        viewModelScope.launch {
            loginRepository.patchProfileEdit(
                nickName,
                allowed,
                intro,
                url,
            ).collectLatest {
                _profileEditSuccess.value = it
            }
        }
    }

    fun getNickNameDoubleCheck(nickName: String) {
        viewModelScope.launch {
            loginRepository.getNickNameDoubleCheck(nickName).collectLatest {
                _nickNameDoubleState.value = UiState.Success(it)
                _profileEditSuccess.value = it.isEmpty()
                updateNextNameBtnValidity()
            }
            _nickNameDoubleState.value = UiState.Empty
        }
    }

    fun setNickName(input: String) {
        this._nickName.value = input
        validateNickName(input)
        updateNextNameBtnValidity()
    }

    fun setIntroduce(input: String) {
        this._introduceNum.value = input
    }

    private fun validateNickName(nickName: String) {
        _isNickNameValid.value = nicknameRegex.matches(nickName)
    }

    private fun updateNextNameBtnValidity() {
        _isBtnSelected.value =
            (!(_profileEditSuccess.value ?: false) && _isNickNameValid.value == true)
    }

    companion object {
        private const val NICKNAME_PATTERN = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9_]{1,12}$"
        val nicknameRegex = Regex(NICKNAME_PATTERN)
    }
}
