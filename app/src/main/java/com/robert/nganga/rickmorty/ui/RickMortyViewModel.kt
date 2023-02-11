package com.robert.nganga.rickmorty.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.repository.RickMortyRepository
import com.robert.nganga.rickmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class RickMortyViewModel@Inject constructor(
        private val repository: RickMortyRepository
): ViewModel() {

    private var _character : MutableLiveData<Resource<Character>> = MutableLiveData()
    val character: LiveData<Resource<Character>> get() = _character

    val characters: Flow<PagingData<CharacterResponse>> = repository.getAllCharacters()


    fun getCharacterById(id: Int) = viewModelScope.launch {
        _character.postValue(Resource.loading())
        val response  = repository.getCharacterById(id)
        _character.postValue(response)
    }


    //fun searchCharacters(query:String) = repository.searchCharacters(query).cachedIn(viewModelScope)
}