package com.android.marvel.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponseCharacterDTO (
    @Json(name = "code") val code: Int?,
    @Json(name = "status") val status: String?,
    @Json(name = "copyright") val copyright: String?,
    @Json(name = "data") val data: DataDTO?
)

