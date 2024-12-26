package com.android.marvel.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity (
    @PrimaryKey val id: Int,
    val name: String?,
    val description: String?,
    @ColumnInfo(name = "image_path") val imagePath: String?
)