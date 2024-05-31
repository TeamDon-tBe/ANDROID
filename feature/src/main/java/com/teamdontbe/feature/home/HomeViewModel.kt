package com.teamdontbe.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.domain.repository.HomeRepository
import com.teamdontbe.domain.repository.LoginRepository
import com.teamdontbe.domain.repository.UserInfoRepository
import com.teamdontbe.feature.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val homeRepository: HomeRepository,
    private val userInfoRepository: UserInfoRepository,
    private val loginRepository: LoginRepository,
) : ViewModel() {

    private val _getFeedDetail = MutableSharedFlow<UiState<FeedEntity>>()
    val getFeedDetail: SharedFlow<UiState<FeedEntity>> = _getFeedDetail

    private val _deleteFeed = MutableSharedFlow<UiState<Boolean>>()
    val deleteFeed: SharedFlow<UiState<Boolean>> = _deleteFeed

    private val _postFeedLiked = MutableSharedFlow<UiState<Boolean>>()
    val postFeedLiked: SharedFlow<UiState<Boolean>> get() = _postFeedLiked

    private val _deleteFeedLiked = MutableSharedFlow<UiState<Boolean>>()
    val deleteFeedLiked: SharedFlow<UiState<Boolean>> get() = _deleteFeedLiked

    private val _postCommentPosting = MutableSharedFlow<UiState<Boolean>>()
    val postCommentPosting: SharedFlow<UiState<Boolean>> get() = _postCommentPosting

    private val _deleteComment = MutableSharedFlow<UiState<Boolean>>()
    val deleteComment: SharedFlow<UiState<Boolean>> get() = _deleteComment

    private val _postTransparent = MutableSharedFlow<UiState<Boolean>>()
    val postTransparent: SharedFlow<UiState<Boolean>> get() = _postTransparent

    private val _openHomeDetail = MutableLiveData<Event<FeedEntity>>()
    val openHomeDetail: LiveData<Event<FeedEntity>> = _openHomeDetail

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    private var _pushAlarmAllowedStatus = MutableStateFlow(false)
    val pushAlarmAllowedStatus: StateFlow<Boolean> get() = _pushAlarmAllowedStatus

    private val _postComplaint = MutableSharedFlow<Boolean>()
    val postComplaint: SharedFlow<Boolean> get() = _postComplaint

    fun saveIsPushAlarmAllowed(isPushAlarmAllowed: Boolean) =
        userInfoRepository.saveIsPushAlarmAllowed(isPushAlarmAllowed)

    fun getIsPushAlarmAllowed() = userInfoRepository.getIsPushAlarmAllowed()

    fun setPhotoUri(uri: String?) {
        _photoUri.value = uri
    }

    fun openHomeDetail(feedEntity: FeedEntity) {
        _openHomeDetail.value = Event(feedEntity)
    }

    fun getFeedList() = homeRepository.getFeedList()

    fun getFeedDetail(contentId: Int) = viewModelScope.launch {
        _getFeedDetail.emit(UiState.Loading)
        homeRepository.getFeedLDetail(contentId)
            .fold(
                { _getFeedDetail.emit(if (it != null) UiState.Success(it) else UiState.Failure("null")) },
                { _getFeedDetail.emit(UiState.Failure(it.message.toString())) }
            )
    }

    fun getCommentList(contentId: Int) = homeRepository.getCommentList(contentId)

    fun deleteFeed(contentId: Int) = viewModelScope.launch {
        _deleteFeed.emit(UiState.Loading)
        homeRepository.deleteFeed(contentId).fold(
            { _deleteFeed.emit(UiState.Success(true)) },
            { _deleteFeed.emit(UiState.Failure(it.message.toString())) }
        )
    }

    fun getMemberId() = userInfoRepository.getMemberId()

    fun getUserNickname() = userInfoRepository.getNickName()

    fun getUserProfile() = userInfoRepository.getMemberProfileUrl()

    fun postFeedLiked(contentId: Int) = viewModelScope.launch {
        homeRepository.postFeedLiked(contentId).fold({ }, {})
    }

    fun deleteFeedLiked(contentId: Int) = viewModelScope.launch {
        homeRepository.deleteFeedLiked(contentId).fold({ }, { })
    }

    fun postCommentPosting(
        contentId: Int,
        commentText: String,
        uriString: String?
    ) {
        viewModelScope.launch {
            homeRepository.postCommentPosting(contentId, commentText, uriString)
                .onSuccess {
                    if (it) _postCommentPosting.emit(UiState.Success(it))
                    else _postCommentPosting.emit(UiState.Failure("포스팅 실패"))
                }
                .onFailure {
                    _postCommentPosting.emit(UiState.Failure(it.message.toString()))
                }
        }
    }

    fun deleteComment(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteComment(commentId).fold(
            { _deleteComment.emit(UiState.Success(it)) },
            { _deleteComment.emit(UiState.Failure(it.message.toString())) }
        )
    }

    fun postCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.postCommentLiked(commentId).fold({}, {})
    }

    fun deleteCommentLiked(commentId: Int) = viewModelScope.launch {
        homeRepository.deleteCommentLiked(commentId).fold({}, {})
    }

    fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ) = viewModelScope.launch {
        _postTransparent.emit(UiState.Loading)
        homeRepository.postTransparent(
            alarmTriggerType,
            targetMemberId,
            alarmTriggerId,
            ghostReason
        ).fold({
            _postTransparent.emit(UiState.Success(true))
        }, { _postTransparent.emit(UiState.Failure(it.message.toString())) })
    }

    fun patchUserProfileUri(info: ProfileEditInfoEntity, url: File? = null) {
        viewModelScope.launch {
            loginRepository.patchProfileUriEdit(
                info,
                url
            ).onSuccess {
                info.isPushAlarmAllowed?.let { _pushAlarmAllowedStatus.value = it }
            }.onFailure {
                Timber.d("fail", it.message.toString())
            }
        }
    }

    fun postComplaint(
        reportTargetNickname: String,
        relateText: String
    ) {
        viewModelScope.launch {
            homeRepository.postComplaint(
                reportTargetNickname,
                relateText
            ).fold({ _postComplaint.emit(true) }, { _postComplaint.emit(false) })
        }
    }
}
