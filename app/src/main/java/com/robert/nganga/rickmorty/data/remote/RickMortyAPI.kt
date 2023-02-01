package com.robert.nganga.rickmorty.data.remote

import com.robert.nganga.rickmorty.model.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyAPI {

@GET("character/{character_id}")
suspend fun getCharacter(
    @Path("character_id")
    characterId: Int
): Response<CharacterResponse>


}