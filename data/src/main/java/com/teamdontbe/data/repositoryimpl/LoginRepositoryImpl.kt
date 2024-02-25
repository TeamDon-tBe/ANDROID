package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.request.RequestProfileEditDto
import com.teamdontbe.domain.entity.LoginEntity
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File
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

    override suspend fun getNickNameDoubleCheck(nickName: String): Flow<Boolean> {
        return flow {
            val result =
                runCatching {
                    loginDataSource.getNickNameDoubleCheck(nickName).success
                }
            emit(result.getOrDefault(false))
        }
    }

    override suspend fun patchProfileEdit(
        nickName: String,
        allowed: Boolean?,
        intro: String,
        url: String?,
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

    override suspend fun patchProfileUriEdit(
        info: ProfileEditInfoEntity,
        file: File?
    ): Result<Boolean> {
        return runCatching {
            // ProfileEditInfoEntity를 JSON 형식의 문자열로 변환하여 요청 본문에 추가
            val infoJson = JSONObject().apply {
                put("nickname", info.nickname)
                put("isAlarmAllowed", info.isAlarmAllowed)
                put("memberIntro", info.memberIntro)
            }.toString()

            // ProfileEditInfoEntity를 RequestBody로 변환
            val infoRequestBody = infoJson.toRequestBody("application/json".toMediaTypeOrNull())

            // 파일 데이터를 읽어와서 별도의 파트로 추가
            val filePart = file?.let {
                val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", it.name, requestBody)
            }

            loginDataSource.patchUserProfilePart(infoRequestBody, filePart).success
        }
    }
}
