package com.android.marvel.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ThumbnailDTO (
    @Json(name = "path") val path: String?,
    @Json(name = "extension") val extension: String?
)