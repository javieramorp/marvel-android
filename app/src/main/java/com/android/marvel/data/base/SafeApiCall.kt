package com.android.marvel.data.base

import com.squareup.moshi.JsonDataException
import com.android.marvel.domain.base.FailureError
import com.android.marvel.domain.base.Resource
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

interface SafeApiCall {

    suspend fun <A, D> safeApiCall(mapper: Mapper<A, D>, apiCall: suspend () -> A): Resource<D> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiCall.invoke()
                val model = mapper.map(result)
                Resource.Success(model)
            } catch (throwable: Throwable) {
                handle(throwable)
            }
        }
    }

    suspend fun <A, D> safeApiListCall(mapper: Mapper<A, D>, apiCall: suspend () -> List<A>): Resource<List<D>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiCall.invoke()
                val modelList = ListMapper(mapper).map(result)
                Resource.Success(modelList)
            } catch (throwable: Throwable) {
                handle(throwable)
            }
        }
    }

    private fun handle(throwable: Throwable): Resource.Failure {
        return when (throwable) {
            is HttpException -> {
                val errorResponse = ErrorResponseMapper.map(throwable)
                handleHttpException(throwable.code(), errorResponse?.message)
            }
            is JsonDataException, is JsonEncodingException, is ParseException -> Resource.Failure(FailureError.Mapping)
            is SocketTimeoutException, is UnknownHostException, is IOException -> Resource.Failure(FailureError.Network)
            else -> Resource.Failure(FailureError.Generic)
        }
    }

    private fun handleHttpException(code: Int, message: String? = ""): Resource.Failure {
        return when(code) {
            HttpCode.MISSING_API_KEY_OR_HASH_OR_TIMESTAMP -> Resource.Failure(FailureError.InvalidApiKeyOrHashOrTimestamp, message)
            HttpCode.INVALID_REFER_OR_HASH -> Resource.Failure(FailureError.InvalidReferOrHash, message)
            HttpCode.METHOD_NOT_ALLOWED -> Resource.Failure(FailureError.Generic, message)
            HttpCode.FORBIDDEN -> Resource.Failure(FailureError.Generic, message)
            else -> Resource.Failure(FailureError.Generic, message)
        }
    }
}