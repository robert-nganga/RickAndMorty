package com.robert.nganga.rickmorty.data.mappers


import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.model.Episode

object CharacterMapper {

    //Convert Character response into a character
    fun buildFrom(response: CharacterResponse, episodes: List<Episode>?): Character{
        return Character(
            episode = episodes,
            gender = response.gender,
            id = response.id,
            image = response.image,
            species = response.species,
            status = response.status,
            name = response.name,
            location = response.location,
            created = response.created,
            origin = response.origin
        )
    }
}