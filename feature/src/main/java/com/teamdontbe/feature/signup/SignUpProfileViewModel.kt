package com.teamdontbe.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.domain.repository.LoginRepository
import com.teamdontbe.domain.repository.MyPageRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
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

    private var _profileEditSuccess = MutableStateFlow<Boolean>(false)
    val profileEditSuccess: SharedFlow<Boolean> get() = _profileEditSuccess

    private var _myPageUserInfo = MutableLiveData<MyPageUserProfileEntity>()
    val myPageUserInfo: LiveData<MyPageUserProfileEntity> get() = _myPageUserInfo

    fun getUserNickName() = userInfoRepository.getNickName()

    private fun saveUserNickNameInLocal(nickName: String) {
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
                .onSuccess { userProfileInfo ->
                    if (userProfileInfo != null) {
                        _myPageUserInfo.value = userProfileInfo
                    }
                }
        }
    }

    fun patchUserProfileUri(info: ProfileEditInfoEntity, url: File?) {
        viewModelScope.launch {
            loginRepository.patchProfileUriEdit(
                info,
                url
            ).onSuccess { patchSuccess ->
                _profileEditSuccess.value = patchSuccess
                if (patchSuccess) {
                    info.nickname?.let { saveUserNickNameInLocal(it) }
                }
            }.onFailure {
                Timber.d("fail", it.message.toString())
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
