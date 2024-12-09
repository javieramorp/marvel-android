package com.android.marvel.data.mappers

import com.android.marvel.data.base.Mapper
import com.android.marvel.data.models.CharacterDTO
import com.android.marvel.domain.models.Character
import java.text.ParseException

class CharacterMapper: Mapper<CharacterDTO, Character> {

    companion object {
        private val TAG = CharacterMapper::class.java.simpleName
    }

    override fun map(model: CharacterDTO): Character {

        val characterId = model.id ?: throw ParseException("$TAG: Character id is null", 0)

        val thumbnail = if (
            model.thumbnail?.path == null ||
            model.thumbnail.path.contains("not_available") ||
            model.thumbnail.extension == null) {
            null
        } else {
            "${model.thumbnail.path}.${model.thumbnail.extension}"
        }

        return Character(
            characterId,
            model.name ?: "",
            model.description ?: "",
            thumbnail)
    }
}