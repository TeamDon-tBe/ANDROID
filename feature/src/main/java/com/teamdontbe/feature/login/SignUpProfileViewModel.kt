package com.teamdontbe.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpProfileViewModel : ViewModel() {
    private var _isBtnSelected = MutableLiveData<Boolean>(false)
    val isBtnSelected: LiveData<Boolean> get() = _isBtnSelected

    private val testNick = "aaa"

    private var _isNickNameValid = MutableLiveData<Boolean>()
    val isNickNameValid: LiveData<Boolean> get() = _isNickNameValid

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> get() = _nickName

    private var _introduceNum = MutableLiveData<String>()
    val introduceNum = _introduceNum

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
        _isBtnSelected.value = (testNick != _nickName.value && _isNickNameValid.value == true)
    }

    companion object {
        private const val NICKNAME_PATTERN = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9_]{1,12}$"
        val nicknameRegex = Regex(NICKNAME_PATTERN)
    }
}
