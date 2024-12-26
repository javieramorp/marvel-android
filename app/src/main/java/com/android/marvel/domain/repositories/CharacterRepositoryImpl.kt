package com.android.marvel.domain.repositories

import com.android.marvel.data.datasources.CharacterLocaleDatasource
import com.android.marvel.data.datasources.CharacterRemoteDatasource
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import javax.inject.Inject

interface CharacterRepository {
    suspend fun getCharacter(characterId: Int): Resource<Character>
    suspend fun getFavoriteCharacter(characterId: Int): Resource<Character>
    suspend fun getCharacters(nameStartsLetter: String?): Resource<List<Character>>
    suspend fun getFavoriteCharacters(): Resource<List<Character>>
    suspend fun setFavoriteCharacter(character: Character): Resource<Unit>
    suspend fun isFavoriteCharacter(characterId: Int): Resource<Boolean>
    suspend fun deleteFavoriteCharacter(character: Character): Resource<Unit>
}

class CharacterRepositoryImpl @Inject constructor(private val characterRemoteDatasource: CharacterRemoteDatasource,
                                                  private val characterLocaleDatasource: CharacterLocaleDatasource
): CharacterRepository {

    override suspend fun getCharacter(characterId: Int): Resource<Character> =
        characterRemoteDatasource.getCharacter(characterId)

    override suspend fun getCharacters(nameStartsLetter: String?): Resource<List<Character>> =
        characterRemoteDatasource.getCharacters(nameStartsLetter)

    override suspend fun getFavoriteCharacters(): Resource<List<Character>> =
        characterLocaleDatasource.getCharacters()

    override suspend fun getFavoriteCharacter(characterId: Int): Resource<Character> =
        characterLocaleDatasource.getCharacter(characterId)

    override suspend fun setFavoriteCharacter(character: Character): Resource<Unit> =
        characterLocaleDatasource.setFavoriteCharacter(character)

    override suspend fun isFavoriteCharacter(characterId: Int): Resource<Boolean> =
        characterLocaleDatasource.isFavoriteCharacter(characterId)

    override suspend fun deleteFavoriteCharacter(character: Character): Resource<Unit> =
        characterLocaleDatasource.deleteFavoriteCharacter(character)
}