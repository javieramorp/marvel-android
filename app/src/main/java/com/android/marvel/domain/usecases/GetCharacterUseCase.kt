package com.android.marvel.domain.usecases

import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(private val repository: CharacterRepository) {

    suspend fun invoke(characterId: Int): Resource<Character> {
        return when (val result = repository.getCharacter(characterId)) {
            is Resource.Success -> Resource.Success(result.value.first())
            is Resource.Failure -> Resource.Failure(result.type, result.message)
        }
    }
}