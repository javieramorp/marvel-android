package com.android.marvel.common.infrastructure

import com.android.marvel.BuildConfig
import com.android.marvel.common.extensions.md5
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor() : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            val hashDecrypted = BuildConfig.LOCAL_TS + BuildConfig.PRIVATE_API_KEY + BuildConfig.PUBLIC_API_KEY
            val hashEncrypted = hashDecrypted.md5()

            val url = request.url.newBuilder()
                .addQueryParameter("ts", BuildConfig.LOCAL_TS)
                .addQueryParameter("apikey", BuildConfig.PUBLIC_API_KEY)
                .addQueryParameter("hash", hashEncrypted)
                .build()

            val newRequest = request.newBuilder()
                .header("Content-Type", "application/json")
                .url(url)
                .build()

            return chain.proceed(newRequest)
        }
}