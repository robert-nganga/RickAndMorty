package com.robert.nganga.rickmorty.model

import androidx.room.Ignore
import androidx.room.PrimaryKey

data class Character(
    val id: Int,
    val created: String,
    val gender: String,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val origin: Origin?,
    val location: Location?,
    val episode: List<Episode>?,
)
