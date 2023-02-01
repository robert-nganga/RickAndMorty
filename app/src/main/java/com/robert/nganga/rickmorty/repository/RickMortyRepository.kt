package com.robert.nganga.rickmorty.repository

import com.robert.nganga.rickmorty.model.CharacterResponse
import retrofit2.Response

interface RickMortyRepository {

    suspend fun getCharacterById(characterId: Int): Response<CharacterResponse>
}