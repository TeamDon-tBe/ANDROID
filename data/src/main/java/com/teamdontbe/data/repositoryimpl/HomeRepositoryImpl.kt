package com.teamdontbe.data.repositoryimpl

import android.content.ContentResolver
import androidx.paging.PagingData
import com.teamdontbe.data.datasource.HomeDataSource
import com.teamdontbe.data.repositoryimpl.utils.createImagePart
import com.teamdontbe.data.repositoryimpl.utils.extractErrorMessage
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HomeRepositoryImpl
@Inject constructor(
    private val contentResolver: ContentResolver,
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override fun getFeedList(): Flow<PagingData<FeedEntity>> = homeDataSource.getFeedList()

    override suspend fun getFeedLDetail(contentId: Int): Result<FeedEntity?> {
        return runCatching {
            homeDataSource.getFeedDetail(contentId).data?.toFeedEntity()
        }
    }

    override fun getCommentList(contentId: Int): Flow<PagingData<CommentEntity>> =
        homeDataSource.getCommentList(contentId)

    override suspend fun deleteFeed(contentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteFeed(contentId).success
        }
    }

    override suspend fun postFeedLiked(contentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.postFeedLiked(contentId).success
        }
    }

    override suspend fun deleteFeedLiked(contentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteFeedLiked(contentId).success
        }
    }

    override suspend fun deleteComment(commentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteComment(
                commentId,
            ).success
        }
    }

    override suspend fun postCommentLiked(commentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.postCommentLiked(commentId).success
        }
    }

    override suspend fun deleteCommentLiked(commentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteCommentLiked(commentId).success
        }
    }

    override suspend fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ): Result<Boolean> {
        return runCatching {
            homeDataSource.postTransparent(
                alarmTriggerType,
                targetMemberId,
                alarmTriggerId,
                ghostReason
            ).success
        }
    }

    override suspend fun postCommentPosting(
        contentId: Int,
        commentText: String,
        uriString: String?
    ): Result<Boolean> {
        return runCatching {

            val infoRequestBody = createCommentRequestBody(commentText)
            val imagePart = withContext(Dispatchers.IO) {
                createImagePart(contentResolver, uriString)
            }

            homeDataSource.postCommentPosting(
                contentId,
                infoRequestBody,
                imagePart
            ).success
        }.onFailure { throwable ->
            return when (throwable) {
                is HttpException -> Result.failure(IOException(throwable.extractErrorMessage()))
                else -> Result.failure(throwable)
            }
        }
    }

    private fun createCommentRequestBody(commentText: String): RequestBody {
        val infoJson = JSONObject().apply {
            put("commentText", commentText)
            put("notificationTriggerType", COMMENT_VALUE)
        }.toString()

        return infoJson.toRequestBody("application/json".toMediaTypeOrNull())
    }

    companion object {
        private const val COMMENT_VALUE = "comment"
    }
}
