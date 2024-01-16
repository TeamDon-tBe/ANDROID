package com.teamdontbe.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageUserProfileDomainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
@Inject constructor(private val myPageUserProfileRepository: MyPageUserProfileDomainRepository) :
    ViewModel() {
    private val _getMyPageUserProfileState =
        MutableStateFlow<UiState<MyPageUserProfileEntity>>(UiState.Empty)
    val getMyPageUserProfileStat: StateFlow<UiState<MyPageUserProfileEntity>> =
        _getMyPageUserProfileState

    fun getMyPageUserProfileInfo(viewMemberId: Int) = viewModelScope.launch {
        myPageUserProfileRepository.getMyPageUserProfile(viewMemberId).collectLatest {
            if (it != null) {
                _getMyPageUserProfileState.value =
                    UiState.Success(it)
            } else {
                UiState.Empty
            }
        }
        _getMyPageUserProfileState.value = UiState.Loading
    }

    val mockDataList = listOf(
        FeedEntity(
            3, "", "돈비돈비", false, false, 90, 200, 60, "돈비 사랑해", "2시간 전",
        ),
        FeedEntity(
            1, "", "먼지먼징", false, false, 100, 100, 30, "먼지는 성장기", "2시간 전",
        ),
        FeedEntity(
            2, "", "쿼카햄토리", false, false, 80, 50, 10, "쿼카햄은 짜파구리 요리사", "1시간 전",
        ),
        FeedEntity(
            4, "", "보람상쥐", false, false, 100, 100, 30, "진실의 방으로", "2시간 전",
        ),
        FeedEntity(
            5, "", "파주멀당", false, false, 80, 50, 10, "상인", "1시간 전",
        ),
        FeedEntity(
            6, "", "아 예예~", false, false, 90, 200, 60, "yes걸", "2시간 전",
        ),
        FeedEntity(
            6, "", "정언명렁-칸트", false, false, 90, 200, 60, "권나라", "2시간 전",
        ),
        FeedEntity(
            6, "", "희죽희죽", false, false, 90, 200, 60, "희죽", "2시간 전",
        ),
        FeedEntity(
            6, "", "쥬쥬", false, false, 90, 200, 60, "시크릿 공주", "2시간 전",
        ),
        FeedEntity(
            6, "", "상우야 변상해줘", false, false, 90, 200, 60, "새벽팟 등극", "2시간 전",
        ),
        FeedEntity(
            6, "", "미스터 다 빈", false, false, 90, 200, 60, "혹시 콩 좋아해?", "2시간 전",
        ),
        FeedEntity(
            6, "", "이승헌금좀주세요", false, false, 90, 200, 60, "새벽 런닝좌", "2시간 전",
        ),
        FeedEntity(
            6, "", "홍박사님을 아세요?", false, false, 90, 200, 60, "잠 좀 자!!", "2시간 전",
        ),
        FeedEntity(
            6, "", "무소유ㄴ", false, false, 90, 200, 60, "아쿠아리움 재밌었어?", "2시간 전",
        ),
    )
}
