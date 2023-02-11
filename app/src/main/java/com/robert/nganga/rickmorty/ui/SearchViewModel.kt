package com.robert.nganga.rickmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.robert.nganga.rickmorty.repository.SearchRepository
import com.robert.nganga.rickmorty.repository.SearchRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchViewModel@Inject constructor(
    private val repository: SearchRepository
): ViewModel() {

    fun getSearchPagingSource(query: String) = repository.getSearchPagingSource(query).cachedIn(viewModelScope)
}