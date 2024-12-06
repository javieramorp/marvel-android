package com.android.marvel.data.base

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(val code: Int?,
                         val message: String?)