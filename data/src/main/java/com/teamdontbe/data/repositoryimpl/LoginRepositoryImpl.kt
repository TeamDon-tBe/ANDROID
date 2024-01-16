package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.domain.entity.LoginEntity
import com.teamdontbe.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LoginRepositoryImpl
    @Inject
    constructor(
        private val loginDataSource: LoginDataSource,
    ) : LoginRepository {
        override suspend fun login(socialType: String): Flow<LoginEntity?> {
            return flow {
                val result =
                    runCatching {
                        loginDataSource.login(RequestLoginDto(socialType)).data?.toLoginDataEntity()
                    }

                Timber.d(result.toString())
                emit(result.getOrDefault(LoginEntity("", -1, "", "", false)))
            }
        }
    }
