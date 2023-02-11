package com.robert.nganga.rickmorty.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.utils.Constants.ITEMS_PER_PAGE
import com.robert.nganga.rickmorty.utils.Constants.STARTING_PAGE_INDEX
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SearchPagingSource@Inject constructor(
        private val query: String,
        private val api: RickMortyAPI): PagingSource<Int, CharacterResponse>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterResponse> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try{
            val results = api.searchCharacters(page = position, query = query)
            val characters = if(results.body()?.results != null){
                results.body()?.results
            }else{
                emptyList()
            }
            val nextKey = getNextPageKey(results.body()?.info?.next)
            val prevKey = if(position == STARTING_PAGE_INDEX){null}else{position - 1}
            LoadResult.Page(
                data = characters!!,
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

    private fun getNextPageKey(nextPage: String?):Int?{
        val next = nextPage?.substring(nextPage.indexOf("page=")+5, nextPage.indexOf("&"))?.toInt()
        Log.i("SearchPagingSource", "getNextPageKey: $next")
        return next
    }

}