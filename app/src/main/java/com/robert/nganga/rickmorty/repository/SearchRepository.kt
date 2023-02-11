package com.robert.nganga.rickmorty.repository

import androidx.paging.PagingData
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchPagingSource(query: String): Flow<PagingData<CharacterResponse>>
}