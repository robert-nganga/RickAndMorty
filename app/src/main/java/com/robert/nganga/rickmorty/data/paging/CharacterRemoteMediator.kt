package com.robert.nganga.rickmorty.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.robert.nganga.rickmorty.data.local.CharacterDatabase
import com.robert.nganga.rickmorty.data.remote.RickMortyAPI
import com.robert.nganga.rickmorty.model.CharacterRemoteKey
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator@Inject constructor(
    private val api: RickMortyAPI,
    private val database: CharacterDatabase):RemoteMediator<Int, CharacterResponse>() {

    private val characterDao = database.characterDao()
    private val characterRemoteKeysDao = database.characterRemoteKeysDao()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterResponse>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = api.getAllCharacter(page = currentPage).body()?.results!!
            val endOfPaginationReached = response.isEmpty()
            Log.i("CharacterRemoteMediator", "Requested data of size ${response.size} and page $currentPage")

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAll()
                    characterRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.map{ character ->
                    CharacterRemoteKey(
                        id = character.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }
                characterRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                characterDao.insertAll(response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterResponse>
    ): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                characterRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharacterResponse>
    ): CharacterRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                characterRemoteKeysDao.getRemoteKeys(id = character.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharacterResponse>
    ): CharacterRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                characterRemoteKeysDao.getRemoteKeys(id = character.id)
            }
    }


}