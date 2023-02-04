package com.robert.nganga.rickmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.robert.nganga.rickmorty.data.local.CharacterDatabase
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.CharacterResponse
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator@Inject constructor(
    private val api: RickMortyAPI,
    private val database: CharacterDatabase):RemoteMediator<Int, CharacterResponse>() {

    val characterDao = database.characterDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterResponse>
    ): MediatorResult {
        TODO("Not yet implemented")
    }


}