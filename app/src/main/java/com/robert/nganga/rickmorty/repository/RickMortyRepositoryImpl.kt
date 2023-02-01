package com.robert.nganga.rickmorty.repository

import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.CharacterResponse
import retrofit2.Response

class RickMortyRepositoryImpl(private val api: RickMortyAPI): RickMortyRepository {

    override suspend fun getCharacterById(characterId: Int): Response<CharacterResponse> {
        return api.getCharacter(characterId = characterId)
    }
}