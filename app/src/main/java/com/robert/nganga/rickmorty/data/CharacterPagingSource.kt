package com.robert.nganga.rickmorty.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.utils.Constants.ITEMS_PER_PAGE
import com.robert.nganga.rickmorty.utils.Constants.STARTING_PAGE_INDEX
import okio.IOException
import retrofit2.HttpException

class CharacterPagingSource(private val api: RickMortyAPI): PagingSource<Int, CharacterResponse>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterResponse> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try{
            val results = api.getAllCharacter(page = position)
            val characters = results.body()?.results!!
            val nextKey = if(characters.isNullOrEmpty() || characters.size< ITEMS_PER_PAGE){
                null
            }else{
                position + (params.loadSize/ITEMS_PER_PAGE)
            }
            val prevKey = if(position == STARTING_PAGE_INDEX){null}else{position - 1}
            LoadResult.Page(
                data = characters,
                nextKey = nextKey,
                prevKey = prevKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}