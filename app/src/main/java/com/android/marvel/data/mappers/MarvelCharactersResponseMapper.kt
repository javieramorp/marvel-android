package com.android.marvel.data.mappers

import com.android.marvel.data.base.Mapper
import com.android.marvel.data.models.MarvelCharactersResponseDTO
import com.android.marvel.domain.models.Character
import java.text.ParseException

class MarvelCharactersResponseMapper: Mapper<MarvelCharactersResponseDTO, Character> {

    companion object {
        private val TAG = MarvelCharactersResponseMapper::class.java.simpleName
    }

    override fun map(model: MarvelCharactersResponseDTO): Character {
        val character = model.data?.characters?.first() ?: throw ParseException("$TAG: Character id is null", 0)
        return CharacterMapper().map(character)
    }
}