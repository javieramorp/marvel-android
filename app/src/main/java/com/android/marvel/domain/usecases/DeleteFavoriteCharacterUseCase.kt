package com.android.marvel.domain.usecases

import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.repositories.CharacterRepository
import javax.inject.Inject

class DeleteFavoriteCharacterUseCase @Inject constructor(private val repository: CharacterRepository) {

    suspend fun invoke(character: Character): Resource<Unit> = repository.deleteFavoriteCharacter(character)
}