package com.android.marvel.common.di

import com.android.marvel.data.datasources.CharacterLocaleDatasource
import com.android.marvel.data.datasources.CharacterLocaleDatasourceImpl
import com.android.marvel.data.datasources.CharacterRemoteDatasource
import com.android.marvel.data.datasources.CharacterRemoteDatasourceImpl
import com.android.marvel.domain.repositories.CharacterRepository
import com.android.marvel.domain.repositories.CharacterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module()
abstract class AppDataModule {

    @Binds
    @Singleton
    abstract fun bindsCharacterRemoteDataSource(datasource: CharacterRemoteDatasourceImpl): CharacterRemoteDatasource

    @Binds
    @Singleton
    abstract fun bindsCharacterLocaleDataSource(datasource: CharacterLocaleDatasourceImpl): CharacterLocaleDatasource

    @Binds
    @Singleton
    abstract fun bindsCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository
}