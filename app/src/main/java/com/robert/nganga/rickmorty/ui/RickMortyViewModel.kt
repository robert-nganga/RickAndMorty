package com.robert.nganga.rickmorty.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.repository.RickMortyRepositoryImpl
import com.robert.nganga.rickmorty.utils.Constants
import com.robert.nganga.rickmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class RickMortyViewModel@Inject constructor(
        private val repository: RickMortyRepositoryImpl): ViewModel() {

    private var _character : MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    val character: LiveData<Resource<CharacterResponse>> get() = _character

//    val characters: Flow<PagingData<CharacterResponse>> = Pager(
//        config = PagingConfig(Constants.ITEMS_PER_PAGE, enablePlaceholders = false),
//        pagingSourceFactory = {repository.charactersPagingSource()}
//    ).flow
//     .cachedIn(viewModelScope)

    val characters: Flow<PagingData<CharacterResponse>> = repository.getAllCharacters()


    fun getCharacterById(id: Int) = viewModelScope.launch {
        val response  = repository.getCharacterById(id)
        _character.postValue(response)
    }
}