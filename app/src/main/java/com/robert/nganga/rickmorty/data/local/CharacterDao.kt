package com.robert.nganga.rickmorty.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse


@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterResponse>)

    @Query("SELECT * FROM characters")
    fun reposByName(): PagingSource<Int, CharacterResponse>

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}