package com.teamdontbe.data.repositoryimpl.utils

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

fun HttpException.extractErrorMessage(): String {
    if (response()?.code() == 413) return "이미지는 3mb를 넘을 수 없습니다"

    val errorBody: ResponseBody? = response()?.errorBody()
    if (errorBody != null) {
        val error = errorBody.string()
        return try {
            val jsonObject = JSONObject(error)
            jsonObject.getString("message")
        } catch (e: JSONException) {
            "알수 없는 오류가 발생 했습니다"
        }
    }
    return "알수 없는 오류가 발생 했습니다"
}
