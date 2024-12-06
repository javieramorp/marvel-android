package com.android.marvel.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataDTO(
    @Json(name = "results") val characters: List<CharacterDTO>?
)