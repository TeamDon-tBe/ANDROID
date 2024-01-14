package com.teamdontbe.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpProfileViewModel : ViewModel() {
    private var _isNickNameExist = MutableLiveData<Boolean>()
    val isNickNameExist: LiveData<Boolean> get() = _isNickNameExist

    private val testNick = "aaa"

    fun validateNickName(nickName: String) {
        _isNickNameExist.value = testNick == nickName
    }
}
