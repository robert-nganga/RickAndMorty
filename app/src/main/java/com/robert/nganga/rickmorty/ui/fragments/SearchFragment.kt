package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.adapters.CharactersLoadStateAdapter
import com.robert.nganga.rickmorty.adapters.SearchAdapter
import com.robert.nganga.rickmorty.databinding.FragmentSearchBinding
import com.robert.nganga.rickmorty.ui.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
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
        //Only search when user has typed at least 2 characters
        if(currentText.length >=2) {
            searchAdapter.refresh()
            getSearchResults(currentText)
        }
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

        binding.imageButton.setOnClickListener {
            findNavController().navigateUp()
        }

        searchAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_searchFragment_to_characterDetailsFragment, bundle)
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    currentText = newText
                }
                //Creates a simple delay so that the user can finish typing
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 500L)
                return true
            }
        })

        binding.retryButton.setOnClickListener { searchAdapter.retry() }
        searchAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.rvSearch.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.errorMsg.isVisible = loadState.source.refresh is LoadState.Error
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
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
            layoutManager = LinearLayoutManager(requireContext())
            //add footer
            adapter = searchAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter { searchAdapter.retry() }
            )
        }
    }
}