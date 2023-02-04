package com.robert.nganga.rickmorty.data.local

import androidx.room.*
import com.robert.nganga.rickmorty.model.CharacterRemoteKey

@Dao
interface CharacterRemoteKeysDao {

    @Query("SELECT * FROM character_remote_keys_table WHERE id = :id")
    suspend fun getRemoteKeys(id: Int): CharacterRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<CharacterRemoteKey>)

    @Query("DELETE FROM character_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}