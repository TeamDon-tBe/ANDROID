package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.domain.entity.AuthEntity
import com.teamdontbe.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LoginRepositoryImpl
    @Inject
    constructor(
        private val authDataSource: LoginDataSource,
    ) : LoginRepository {
        override suspend fun login(socialType: String): Flow<AuthEntity?> {
            return flow {
                val result =
                    runCatching {
                        authDataSource.login(RequestLoginDto(socialType)).data?.toAuthDataEntity()
                    }

                Timber.d(result.toString())
                emit(result.getOrDefault(AuthEntity("", -1, "", "", false)))
            }
        }
    }
