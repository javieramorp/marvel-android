package com.android.marvel.data.base

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class ErrorResponseMapper {

    companion object {
        fun map(throwable: HttpException): ErrorResponse? {
            try {
                val error = throwable.response()?.errorBody()?.byteStream()?.bufferedReader().use { it?.readText() }
                val adapter: JsonAdapter<ErrorResponse> = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                error?.let {
                    return adapter.fromJson(error)
                }

            } catch (exception: Exception) {
                return null
            }
            return null
        }
    }
}