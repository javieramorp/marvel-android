package com.android.marvel.domain.usecases

import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.repositories.CharacterRepository
import javax.inject.Inject

class IsFavoriteCharacterUseCase @Inject constructor(private val repository: CharacterRepository) {

    suspend fun invoke(characterId: Int): Resource<Boolean> = repository.isFavoriteCharacter(characterId)
}