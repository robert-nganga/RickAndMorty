package com.robert.nganga.rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robert.nganga.rickmorty.data.paging.CharacterRemoteMediator
import com.robert.nganga.rickmorty.data.local.CharacterDatabase
import com.robert.nganga.rickmorty.data.mappers.CharacterMapper
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.model.Episode
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

//    fun charactersPagingSource() = CharacterPagingSource(api)

    @OptIn(ExperimentalPagingApi::class)
    fun getAllCharacters(): Flow<PagingData<CharacterResponse>>{
        val pagingSourceFactory = {database.characterDao().reposByName()}
        return Pager(
            config = PagingConfig(Constants.ITEMS_PER_PAGE, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(api, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getCharacterById(characterId: Int): Resource<Character> {
        val response = safeApiCall { api.getCharacter(characterId) }

        return if (response.status ==  Resource.Status.SUCCESS) {
            val episodeList = response.data?.let { getEpisodesFromCharacterResponse(it) }
            val character = CharacterMapper.buildFrom(response.data!!, episodeList)
            Resource.success(character)
        }else{ Resource.error(response.message!!) }

    }

    override suspend fun getEpisodeRange(episodeRange: String): Resource<List<Episode>> {
        return safeApiCall { api.getEpisodeRange(episodeRange) }
    }

    override suspend fun getEpisode(episodeId: Int): Resource<Episode> {
        return safeApiCall { api.getEpisode(episodeId) }
    }

    private suspend fun getEpisodesFromCharacterResponse(response: CharacterResponse): List<Episode>{
        // Extract the episode id at the end of each episode url and put them in a list
        val episodeRange = response.episode?.map {
            it.substring(it.lastIndexOf("/") + 1)
        }.toString()
        val episodesResponse = api.getEpisodeRange(episodeRange)

        return if (episodesResponse.isSuccessful){
            episodesResponse.body()!!
        }else{ emptyList()}
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): Resource<T>{
        return try {
            val responseStatus = apiCall.invoke()
            if (responseStatus.isSuccessful){
                Resource.success(responseStatus.body()!!)
            }else{
                Resource.error(responseStatus.message())
            }
        } catch (e: Exception){
            when(e){
                is IOException ->{Resource.error(e.message!!)}
                is HttpException ->{Resource.error(e.message!!)}
                else -> {Resource.error(e.message!!)}
            }

        }
    }
}