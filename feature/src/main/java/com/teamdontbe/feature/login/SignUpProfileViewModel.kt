package com.teamdontbe.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpProfileViewModel : ViewModel() {
    private var _isBtnSelected = MutableLiveData<Boolean>()
    val isBtnSelected: LiveData<Boolean> get() = _isBtnSelected

    private val testNick = "aaa"

    private var _isNickNameValid = MutableLiveData<Boolean>()
    val isNickNameValid: LiveData<Boolean> get() = _isNickNameValid

    private val nickName = MutableLiveData<String>()

    private var _nickNameNum = MutableLiveData<Int>(0)
    val nickNameNum = _nickNameNum

    fun setNickName(input: String) {
        this.nickName.value = input
        validateNickName(input)
        updateNickNameBtnValidity(input)
        updateNickNameNum()
    }

    private fun validateNickName(nickName: String) {
        _isNickNameValid.value = nicknameRegex.matches(nickName)
    }

    fun updateNickNameBtnValidity(nickName: String) {
        _isBtnSelected.value = (testNick == nickName && _isNickNameValid.value == true)
    }

    fun updateNickNameNum() {
        _nickNameNum.value = nickName.value?.length ?: 0
    }

    companion object {
        private const val NICKNAME_PATTERN = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9_]{1,12}$"
        val nicknameRegex = Regex(NICKNAME_PATTERN)
    }
}
