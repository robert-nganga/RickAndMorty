package com.robert.nganga.rickmorty.data.remote

import com.robert.nganga.rickmorty.data.remote.response.AllCharactersResponse
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyAPI {

    @GET("character/{character_id}")
    suspend fun getCharacter(
        @Path("character_id")
        characterId: Int
    ): Response<CharacterResponse>

    @GET("character/")
    suspend fun getAllCharacter(
        @Query("page")
        page: Int
    ): Response<AllCharactersResponse>

    @GET("episode/{episode_range}")
    suspend fun getEpisodeRange(
        @Path("episode_range")
        episodeRange: String
    ): Response<List<Episode>>

    @GET("character/{episode_id}")
    suspend fun getEpisode(
        @Path("episode_id")
        episodeId: Int
    ): Response<Episode>


}