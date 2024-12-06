package com.android.marvel.data.datasources

import com.android.marvel.data.api.MarvelApiClient
import com.android.marvel.data.base.BaseDataSource
import com.android.marvel.data.mappers.CharacterMapper
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import javax.inject.Inject

interface CharacterDatasource {
    suspend fun getCharacter(characterId: Int): Resource<List<Character>>
    suspend fun getCharacters(nameStartsLetter: String?, limit: Int = 100): Resource<List<Character>>
}

class CharacterDatasourceImpl @Inject constructor(private val marvelApiClient: MarvelApiClient): CharacterDatasource, BaseDataSource() {

    override suspend fun getCharacter(characterId: Int): Resource<List<Character>> = safeApiListCall(CharacterMapper()) {
        marvelApiClient.getCharacter(characterId).data?.characters ?: listOf()
    }

    override suspend fun getCharacters(nameStartsLetter: String?, limit: Int): Resource<List<Character>> = safeApiListCall(CharacterMapper()) {
        marvelApiClient.getCharacters(nameStartsLetter, limit).data?.characters ?: listOf()
    }
}