package com.robert.nganga.rickmorty.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.repository.RickMortyRepositoryImpl
import kotlinx.coroutines.launch

class RickMortyViewModel(private val repository: RickMortyRepositoryImpl): ViewModel() {

    private var _character : MutableLiveData<CharacterResponse> = MutableLiveData()
    val character: LiveData<CharacterResponse> get() = _character


    fun getCharacterById(id: Int) = viewModelScope.launch {
        val response  = repository.getCharacterById(id)
        if (response.isSuccessful){
            _character.postValue(response.body())
        }
    }
}