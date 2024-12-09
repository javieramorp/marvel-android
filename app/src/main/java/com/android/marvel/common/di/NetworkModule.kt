package com.android.marvel.common.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.android.marvel.BuildConfig
import com.android.marvel.common.infrastructure.RequestInterceptor
import com.android.marvel.data.api.MarvelApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Singleton
    @Provides
    fun provideHttpClientBuilder(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(45, TimeUnit.SECONDS)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi.Builder {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
    }

    @Singleton
    @Provides
    fun provideInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    @Singleton
    @Provides
    fun provideMockRetrofit(httpClient: OkHttpClient.Builder, moshi: Moshi.Builder, interceptor: RequestInterceptor): Retrofit {
        return Retrofit.Builder()
            .client(httpClient.addInterceptor(interceptor).build())
            .baseUrl(BuildConfig.BASE_URL_MARVEL)
            .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
            .build()
    }

    @Singleton
    @Provides
    fun provideMockApiClient(retrofit: Retrofit): MarvelApiClient {
        return retrofit.create(MarvelApiClient::class.java)
    }
}