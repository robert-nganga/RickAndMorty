package com.robert.nganga.rickmorty.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "characters")
data class CharacterResponse(
    @PrimaryKey
    val id: Int,
    val created: String,
    val gender: String,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
    val url: String,
    @Ignore val origin: Origin?,
    @Ignore val location: Location?,
    @Ignore val episode: List<String>?,
){
    constructor(
        id: Int,
        created: String,
        gender: String,
        image: String,
        name: String,
        species: String,
        status: String,
        type: String,
        url: String
    ): this(id, created, gender, image, name, species,
        status, type, url, null, null, null)
}