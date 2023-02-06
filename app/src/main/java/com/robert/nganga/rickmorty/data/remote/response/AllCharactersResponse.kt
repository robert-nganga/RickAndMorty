package com.robert.nganga.rickmorty.data.remote.response

import com.robert.nganga.rickmorty.model.Info

data class AllCharactersResponse(
    val info: Info,
    val results: List<CharacterResponse>
)