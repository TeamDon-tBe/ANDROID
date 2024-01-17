package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.PostingDataSource
import com.teamdontbe.data.dto.request.RequestPostingDto
import com.teamdontbe.domain.repository.PostingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class PostingRepositoryImpl
    @Inject
    constructor(
        private val postingDataSource: PostingDataSource,
    ) : PostingRepository {
        override suspend fun posting(contentText: String): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        postingDataSource.posting(RequestPostingDto(contentText)).success
                    }
                Timber.d(result.toString())
                emit(result.getOrDefault(false))
            }
        }
    }
