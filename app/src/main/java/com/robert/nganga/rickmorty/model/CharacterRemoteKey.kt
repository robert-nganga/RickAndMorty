package com.robert.nganga.rickmorty.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robert.nganga.rickmorty.utils.Constants.CHARACTER_REMOTE_KEYS_TABLE


@Entity(tableName = CHARACTER_REMOTE_KEYS_TABLE)
data class CharacterRemoteKey(
    @PrimaryKey
    val id : Int,
    val prevKey: Int?,
    val nextKey: Int?
)