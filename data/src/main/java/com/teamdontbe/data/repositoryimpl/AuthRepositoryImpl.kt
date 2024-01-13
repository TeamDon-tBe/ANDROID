package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.AuthDataSource
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.domain.entity.AuthEntity
import com.teamdontbe.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authDataSource: AuthDataSource,
    ) : AuthRepository {
        override suspend fun login(
            auth: String,
            socialType: String,
        ): Flow<AuthEntity?> {
            return flow {
                val result =
                    runCatching {
                        authDataSource.login(auth, RequestLoginDto(socialType)).data?.toAuthDataEntity()
                    }

                Timber.d(result.toString())
                emit(result.getOrDefault(AuthEntity("", -1, "", "", false)))
            }
        }
    }
