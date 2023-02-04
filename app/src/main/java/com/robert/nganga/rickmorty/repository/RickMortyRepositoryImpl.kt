package com.robert.nganga.rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robert.nganga.rickmorty.data.CharacterPagingSource
import com.robert.nganga.rickmorty.data.CharacterRemoteMediator
import com.robert.nganga.rickmorty.data.local.CharacterDatabase
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.utils.Constants
import com.robert.nganga.rickmorty.utils.Resource
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RickMortyRepositoryImpl@Inject constructor(
        private val database: CharacterDatabase,
        private val api: RickMortyAPI): RickMortyRepository {

    fun charactersPagingSource() = CharacterPagingSource(api)

    @OptIn(ExperimentalPagingApi::class)
    fun getAllCharacters(): Flow<PagingData<CharacterResponse>>{
        val pagingSourceFactory = {database.characterDao().reposByName()}
        return Pager(
            config = PagingConfig(Constants.ITEMS_PER_PAGE, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(api, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getCharacterById(characterId: Int): Resource<CharacterResponse> {
        return safeApiCall { api.getCharacter(characterId) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): Resource<T>{
        return try {
            Resource.Success(apiCall.invoke().body()!!)
        } catch (e: Exception){
            when(e){
                is IOException ->{Resource.Error("Network Error")}
                is HttpException ->{Resource.Error("Connection Failure")}
                else -> {Resource.Error(e.message!!)}
            }

        }
    }
}