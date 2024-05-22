package com.teamdontbe.data.repositoryimpl

import android.content.ContentResolver
import com.teamdontbe.data.datasource.PostingDataSource
import com.teamdontbe.data.dto.request.RequestPostingDto
import com.teamdontbe.data.repositoryimpl.utils.createImagePart
import com.teamdontbe.data.repositoryimpl.utils.extractErrorMessage
import com.teamdontbe.domain.repository.PostingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostingRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    private val postingDataSource: PostingDataSource,
) : PostingRepository {
    override suspend fun posting(requestPosting: String): Flow<Boolean> {
        return flow {
            val result =
                runCatching {
                    postingDataSource.posting(RequestPostingDto(requestPosting)).success
                }
            emit(result.getOrDefault(false))
        }
    }

    override suspend fun postingMultiPart(content: String, uriString: String?): Result<Boolean> {
        return runCatching {
            val textRequestBody = createContentRequestBody(content)

            val imagePart = withContext(Dispatchers.IO) {
                createImagePart(contentResolver, uriString)
            }

            postingDataSource.postingMultiPart(textRequestBody, imagePart).success

        }.onFailure { throwable ->
            return when (throwable) {
                is HttpException -> Result.failure(IOException(throwable.extractErrorMessage()))
                else -> Result.failure(throwable)
            }
        }
    }

    private fun createContentRequestBody(content: String): RequestBody {
        val contentJson = JSONObject().apply { put("contentText", content) }.toString()
        return contentJson.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
