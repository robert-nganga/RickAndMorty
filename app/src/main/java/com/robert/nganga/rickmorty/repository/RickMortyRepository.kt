package com.robert.nganga.rickmorty.repository

import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.utils.Resource

interface RickMortyRepository {

    suspend fun getCharacterById(characterId: Int): Resource<CharacterResponse>
}