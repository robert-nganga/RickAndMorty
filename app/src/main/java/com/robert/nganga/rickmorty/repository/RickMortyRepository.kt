package com.robert.nganga.rickmorty.repository

import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.model.Episode
import com.robert.nganga.rickmorty.utils.Resource

interface RickMortyRepository {

    suspend fun getCharacterById(characterId: Int): Resource<Character>

    suspend fun getEpisodeRange(episodeRange: String): Resource<List<Episode>>

    suspend fun getEpisode(episodeId: Int): Resource<Episode>

}