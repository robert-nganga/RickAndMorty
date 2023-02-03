package com.robert.nganga.rickmorty.repository

import com.robert.nganga.rickmorty.data.CharacterPagingSource
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.utils.Resource
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RickMortyRepositoryImpl@Inject constructor(
        private val api: RickMortyAPI): RickMortyRepository {

    fun charactersPagingSource() = CharacterPagingSource(api)

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