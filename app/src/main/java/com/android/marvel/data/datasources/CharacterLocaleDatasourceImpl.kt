package com.android.marvel.data.datasources

import com.android.marvel.data.database.CharacterDao
import com.android.marvel.data.models.CharacterEntity
import com.android.marvel.domain.base.FailureError
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import javax.inject.Inject

interface CharacterLocaleDatasource {
    suspend fun getCharacter(characterId: Int): Resource<Character>
    suspend fun getCharacters(): Resource<List<Character>>
    suspend fun setFavoriteCharacter(character: Character): Resource<Unit>
    suspend fun isFavoriteCharacter(characterId: Int): Resource<Boolean>
    suspend fun deleteFavoriteCharacter(character: Character): Resource<Unit>
}

class CharacterLocaleDatasourceImpl @Inject constructor (
    private val characterDao: CharacterDao
) : CharacterLocaleDatasource {

    override suspend fun getCharacter(characterId: Int): Resource<Character> {
        return try {
            val entity = characterDao.getCharacter(characterId)
            if (entity != null) {
                Resource.Success(entity.toCharacter())
            } else {
                Resource.Failure(FailureError.NotFound)
            }
        } catch (e: Exception) {
            Resource.Failure(FailureError.DatabaseError, e.printStackTrace().toString())
        }
    }

    override suspend fun getCharacters(): Resource<List<Character>> {
        return try {
            val entities = characterDao.getCharacters()
            Resource.Success(entities.map { it.toCharacter() })
        } catch (e: Exception) {
            Resource.Failure(FailureError.DatabaseError, e.printStackTrace().toString())
        }
    }

    override suspend fun setFavoriteCharacter(character: Character): Resource<Unit> {
        return try {
            val entity = character.toEntity()
            characterDao.insertCharacter(entity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(FailureError.Generic, e.printStackTrace().toString())
        }
    }

    override suspend fun isFavoriteCharacter(characterId: Int): Resource<Boolean> {
        return try {
            Resource.Success(characterDao.getCharacter(characterId) != null)
        } catch (e: Exception) {
            Resource.Failure(FailureError.DatabaseError, e.printStackTrace().toString())
        }
    }

    override suspend fun deleteFavoriteCharacter(character: Character): Resource<Unit> {
        return try {
            val entity = character.toEntity()
            characterDao.deleteCharacter(entity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(FailureError.DatabaseError, e.printStackTrace().toString())
        }
    }

    private fun CharacterEntity.toCharacter(): Character {
        return Character(
            id = id,
            name = name ?: "",
            description = description ?: "",
            thumbnail = imagePath
        )
    }

    private fun Character.toEntity(): CharacterEntity {
        return CharacterEntity(
            id = id,
            name = name,
            description = description,
            imagePath = thumbnail,
        )
    }
}
