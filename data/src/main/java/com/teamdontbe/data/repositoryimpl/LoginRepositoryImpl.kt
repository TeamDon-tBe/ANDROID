package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.request.RequestProfileEditDto
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
        override suspend fun postLogin(socialType: String): Flow<LoginEntity?> {
            return flow {
                val result =
                    runCatching {
                        loginDataSource.postLogin(RequestLoginDto(socialType)).data?.toLoginDataEntity()
                    }

                Timber.d(result.toString())
                emit(result.getOrDefault(LoginEntity("", -1, "", "", "", false)))
            }
        }

        override suspend fun getNickNameDoubleCheck(nickName: String): Flow<String> {
            return flow {
                val result =
                    runCatching {
                        loginDataSource.getNickNameDoubleCheck(nickName).message
                    }
                emit(result.getOrDefault(""))
            }
        }

        override suspend fun patchProfileEdit(
            nickName: String,
            allowed: Boolean,
            intro: String,
            url: String,
        ): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        loginDataSource.patchProfileEdit(
                            RequestProfileEditDto(
                                nickName,
                                allowed,
                                intro,
                                url,
                            ),
                        ).success
                    }
                emit(result.getOrDefault(false))
            }
        }
    }
