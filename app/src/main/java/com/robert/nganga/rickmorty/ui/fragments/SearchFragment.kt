package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.adapters.SearchAdapter
import com.robert.nganga.rickmorty.databinding.FragmentSearchBinding
import com.robert.nganga.rickmorty.ui.MainActivity
import com.robert.nganga.rickmorty.ui.RickMortyViewModel
import com.robert.nganga.rickmorty.ui.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    private var currentText = ""
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable{
        searchAdapter.refresh()
        if(currentText.isNotEmpty()) {
            getSearchResults(currentText)
        }
        println(currentText)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = SearchAdapter()

        binding.bindRecyclerview(searchAdapter)

        var job: Job? = null
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Creates a simple delay so that the user can finish typing
                if (newText != null) {
                    currentText = newText
                }
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 500L)
//                job?.cancel()
//                job = MainScope().launch {
//                    delay(500L)
//                    newText?.let{ query ->
//                        if(query.isNotEmpty()){
//                            getSearchResults(query)
//                        }
//                    }
//                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSearchResults(query: String) = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.getSearchPagingSource(query).collect{
                searchAdapter.submitData(it)
            }
        }
    }

    private fun FragmentSearchBinding.bindRecyclerview(searchAdapter: SearchAdapter){
        rvSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}