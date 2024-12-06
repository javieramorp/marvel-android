package com.android.marvel.common.di

import android.content.Context
import com.android.marvel.common.infrastructure.ResourcesAccessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideResourcesAccessor(@ApplicationContext context: Context): ResourcesAccessor {
        return ResourcesAccessor(context)
    }
}
