package com.robert.nganga.rickmorty.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.repository.RickMortyRepositoryImpl
import com.robert.nganga.rickmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class RickMortyViewModel@Inject constructor(
        private val repository: RickMortyRepositoryImpl): ViewModel() {

    private var _character : MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    val character: LiveData<Resource<CharacterResponse>> get() = _character


    fun getCharacterById(id: Int) = viewModelScope.launch {
        val response  = repository.getCharacterById(id)
        _character.postValue(response)
    }
}