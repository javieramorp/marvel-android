package com.android.marvel.domain.repositories

import com.android.marvel.data.datasources.CharacterDatasource
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import javax.inject.Inject

interface CharacterRepository {
    suspend fun getCharacter(characterId: Int): Resource<Character>
    suspend fun getCharacters(nameStartsLetter: String?): Resource<List<Character>>
}

class CharacterRepositoryImpl @Inject constructor(private val datasource: CharacterDatasource): CharacterRepository {

    override suspend fun getCharacter(characterId: Int): Resource<Character> =
        datasource.getCharacter(characterId)

    override suspend fun getCharacters(nameStartsLetter: String?): Resource<List<Character>> =
        datasource.getCharacters(nameStartsLetter)
}