package com.android.marvel.data.mappers

import com.android.marvel.data.base.Mapper
import com.android.marvel.data.models.CharacterDTO
import com.android.marvel.domain.models.Character

class CharacterMapper: Mapper<CharacterDTO, Character> {

    override fun map(model: CharacterDTO): Character {


        val thumbnail = if (model.thumbnail?.path == null || model.thumbnail.path.contains("not_available") || model.thumbnail.extension == null) null else "${model.thumbnail.path}.${model.thumbnail.extension}"
        return Character(
            model.id ?: 0,
            model.name ?: "",
            model.description ?: "",
            thumbnail)
    }
}