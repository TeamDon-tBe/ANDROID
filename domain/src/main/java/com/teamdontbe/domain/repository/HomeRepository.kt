package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getFeedList(): Flow<List<FeedEntity>?>
}
