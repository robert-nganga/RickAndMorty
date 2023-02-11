package com.robert.nganga.rickmorty.repository

import androidx.paging.PagingData
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.model.Episode
import com.robert.nganga.rickmorty.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RickMortyRepository {

    fun getAllCharacters(): Flow<PagingData<CharacterResponse>>

    suspend fun getCharacterById(characterId: Int): Resource<Character>

    suspend fun getEpisodeRange(episodeRange: String): Resource<List<Episode>>

    suspend fun getEpisode(episodeId: Int): Resource<Episode>

}