package com.robert.nganga.rickmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robert.nganga.rickmorty.model.CharacterRemoteKey
import com.robert.nganga.rickmorty.model.CharacterResponse


@Database(
    entities = [CharacterResponse::class, CharacterRemoteKey::class],
    version = 1,
    exportSchema = false
)

abstract class CharacterDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeysDao(): CharacterRemoteKeysDao

}