package com.teamdontbe.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.repository.LoginRepository
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpProfileViewModel
@Inject constructor(
    private val loginRepository: LoginRepository,
    private val userInfoRepository: UserInfoRepository,
    private val myPageRepository: MyPageRepository,
) : ViewModel() {
    private var _isBtnSelected = MutableLiveData<Boolean>(false)
    val isBtnSelected: LiveData<Boolean> get() = _isBtnSelected

    private var _isNickNameValid = MutableLiveData<Boolean>()
    val isNickNameValid: LiveData<Boolean> get() = _isNickNameValid

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> get() = _nickName

    private var _introduceText = MutableLiveData<String>()
    val introduceText = _introduceText

    private val _nickNameDoubleState = MutableSharedFlow<UiState<Boolean>>()
    val nickNameDoubleState: SharedFlow<UiState<Boolean>> = _nickNameDoubleState

    private var _profileEditSuccess = MutableLiveData<Boolean>()
    val profileEditSuccess: LiveData<Boolean> get() = _profileEditSuccess

    private var _myPageUserIntroduce = MutableLiveData<String>()
    val myPageUserIntroduce: LiveData<String> get() = _myPageUserIntroduce

    fun getUserNickName() = userInfoRepository.getNickName()

    fun saveUserNickNameInLocal(nickName: String) {
        userInfoRepository.saveNickName(nickName)
    }

    fun checkOnMyPageInitialNickName() {
        _nickName.value = getUserNickName()
        _isNickNameValid.value = nicknameRegex.matches(_nickName.value ?: "")
        _isBtnSelected.value = _isNickNameValid.value == true
    }

    val getMemberId = userInfoRepository.getMemberId()

    fun getUserProfileIntroduce() {
        viewModelScope.launch {
            myPageRepository.getMyPageUserProfile(getMemberId ?: -1)
                .collectLatest { userProfileInfo ->
                    if (userProfileInfo != null) {
                        _myPageUserIntroduce.value = userProfileInfo.memberIntro
                    }
                }
        }
    }

    fun patchUserProfileEdit(
        nickName: String,
        allowed: Boolean?,
        intro: String,
        url: String?,
    ) {
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
                _nickNameDoubleState.emit(UiState.Success(it))
                _profileEditSuccess.value = !it
                updateNextNameBtnValidity()
            }
            _nickNameDoubleState.emit(UiState.Empty)
        }
    }

    fun setNickName(input: String) {
        // 닉네임 eidtText가 변경 될때 만 해당 로직 처리
        if (_nickName.value != input) {
            this._nickName.value = input
            validateNickName(input)
            _isBtnSelected.value = false
        }
    }

    fun setIntroduce(input: String) {
        this._introduceText.value = input
    }

    private fun validateNickName(nickName: String) {
        _isNickNameValid.value = nicknameRegex.matches(nickName)
        updateNextNameBtnValidity()
    }

    private fun updateNextNameBtnValidity() {
        _isBtnSelected.value =
            _profileEditSuccess.value == false && _isNickNameValid.value == true
    }

    companion object {
        private const val NICKNAME_PATTERN = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9_]{1,12}$"
        val nicknameRegex = Regex(NICKNAME_PATTERN)
    }
}
