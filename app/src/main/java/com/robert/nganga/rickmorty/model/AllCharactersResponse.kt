package com.robert.nganga.rickmorty.model

data class AllCharactersResponse(
    val info: Info,
    val results: List<CharacterResponse>
)