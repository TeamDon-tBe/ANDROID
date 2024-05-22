package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data.datasource.HomeDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestCommentLikedDto
import com.teamdontbe.data.dto.request.RequestTransparentDto
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.data_remote.pagingsourceimpl.HomeCommentPagingSourceImpl
import com.teamdontbe.data_remote.pagingsourceimpl.HomeFeedPagingSourceImpl
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class HomeDataSourceImpl
@Inject
constructor(
    private val homeApiService: HomeApiService,
) : HomeDataSource {
    override fun getFeedList(): Flow<PagingData<FeedEntity>> {
        // PagingConfig(페이지가 로드되는 시점 : 1인 이유는 30개의 마지막 아이템이 보였을 때 로드된다는 의미)
        // 만약에 5로 바뀌면 25번째 아이템이 보일 때 다음 페이지 로드됨
        return Pager(PagingConfig(1)) {
            HomeFeedPagingSourceImpl(homeApiService)
        }.flow
    }

    override suspend fun getFeedDetail(contentId: Int): BaseResponse<ResponseFeedDto> {
        return homeApiService.getFeedDetail(contentId)
    }

    override fun getCommentList(contentId: Int): Flow<PagingData<CommentEntity>> {
        return Pager(PagingConfig(1)) {
            HomeCommentPagingSourceImpl(homeApiService, contentId)
        }.flow
    }

    override suspend fun deleteFeed(contentId: Int): BaseResponse<Unit> {
        return homeApiService.deleteFeed(contentId)
    }

    override suspend fun postFeedLiked(contentId: Int): BaseResponse<Unit> {
        return homeApiService.postLiked(contentId)
    }

    override suspend fun deleteFeedLiked(contentId: Int): BaseResponse<Unit> {
        return homeApiService.deleteLiked(contentId)
    }

    override suspend fun postCommentPosting(
        contentId: Int,
        commentText: RequestBody,
        image: MultipartBody.Part?
    ): BaseResponse<Unit> {
        return homeApiService.postCommentPosting(contentId, commentText, image)
    }

    override suspend fun deleteComment(commentId: Int): BaseResponse<Unit> {
        return homeApiService.deleteComment(commentId)
    }

    override suspend fun postCommentLiked(commentId: Int): BaseResponse<Unit> {
        return homeApiService.postCommentLiked(
            commentId,
            RequestCommentLikedDto("test", "commentLiked"),
        )
    }

    override suspend fun deleteCommentLiked(commentId: Int): BaseResponse<Unit> {
        return homeApiService.deleteCommentLiked(commentId)
    }

        override suspend fun postTransparent(
            alarmTriggerType: String,
            targetMemberId: Int,
            alarmTriggerId: Int,
            ghostReason: String
        ): BaseResponse<Unit> {
            return homeApiService.postTransparent(
                RequestTransparentDto(
                    alarmTriggerType,
                    targetMemberId,
                    alarmTriggerId,
                    ghostReason
                ),
            )
        }
    }
