package com.android.marvel.domain.usecases

import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.repositories.CharacterRepository
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(private val repository: CharacterRepository) {

    suspend fun invoke(nameStartsWith: String?): Resource<List<Character>> = repository.getCharacters(nameStartsWith)
}