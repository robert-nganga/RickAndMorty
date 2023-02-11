package com.robert.nganga.rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robert.nganga.rickmorty.data.cache.CharacterCache
import com.robert.nganga.rickmorty.data.local.CharacterDatabase
import com.robert.nganga.rickmorty.data.mappers.CharacterMapper
import com.robert.nganga.rickmorty.data.paging.CharacterRemoteMediator
import com.robert.nganga.rickmorty.data.paging.SearchPagingSource
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.model.Episode
import com.robert.nganga.rickmorty.utils.Constants
import com.robert.nganga.rickmorty.utils.Constants.ITEMS_PER_PAGE
import com.robert.nganga.rickmorty.utils.DataAccess.safeApiCall
import com.robert.nganga.rickmorty.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class RickMortyRepositoryImpl@Inject constructor(
        private val database: CharacterDatabase,
        private val api: RickMortyAPI): RickMortyRepository {

    // Mutex to make writes to cached values thread-safe.
    private val characterMutex = Mutex()



    @OptIn(ExperimentalPagingApi::class)
    override fun getAllCharacters(): Flow<PagingData<CharacterResponse>>{
        val pagingSourceFactory = {database.characterDao().reposByName()}
        return Pager(
            config = PagingConfig(ITEMS_PER_PAGE, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(api, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getCharacterById(characterId: Int): Resource<Character> {
        val characterCache = characterMutex.withLock { CharacterCache.characterMap[characterId] }

        // Check if the cache is empty and return it if its not
        if (characterCache != null){
            return Resource.success(characterCache)
        }else{
            val response = safeApiCall { api.getCharacter(characterId) }

            return if (response.status ==  Resource.Status.SUCCESS) {
                val episodeList = response.data?.let { getEpisodesFromCharacterResponse(it) }
                val character = CharacterMapper.buildFrom(response.data!!, episodeList)
                CharacterCache.characterMap[characterId] = character
                Resource.success(character)
            }else{ Resource.error(response.message!!) }
        }


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


}