package com.android.marvel.common.di

import com.android.marvel.data.datasources.CharacterDatasource
import com.android.marvel.data.datasources.CharacterDatasourceImpl
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
    abstract fun bindsCharacterDataSource(datasource: CharacterDatasourceImpl): CharacterDatasource

    @Binds
    @Singleton
    abstract fun bindsCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository
}