package com.android.marvel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.marvel.data.models.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}