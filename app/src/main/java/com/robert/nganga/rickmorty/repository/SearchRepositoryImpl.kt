package com.robert.nganga.rickmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robert.nganga.rickmorty.data.paging.SearchPagingSource
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl@Inject constructor(private val api: RickMortyAPI): SearchRepository {

    override  fun getSearchPagingSource(query: String): Flow<PagingData<CharacterResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(query =  query, api = api) }
        ).flow
    }


}