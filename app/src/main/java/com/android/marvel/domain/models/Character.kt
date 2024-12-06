package com.android.marvel.domain.models

data class Character (
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String?
)