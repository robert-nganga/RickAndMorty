package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.adapters.CharactersAdapter
import com.robert.nganga.rickmorty.adapters.CharactersLoadStateAdapter
import com.robert.nganga.rickmorty.databinding.FragmentHomeBinding
import com.robert.nganga.rickmorty.ui.MainActivity
import com.robert.nganga.rickmorty.ui.RickMortyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: RickMortyViewModel
    private lateinit var adapter: CharactersAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        adapter = CharactersAdapter(getDeviceWidth())
        binding.bindAdapter(adapter)
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_characterDetailsFragment, bundle)
        }

//        binding.retryButton.setOnClickListener { repoAdapter.retry() }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                //binding.rvCharacters.isVisible = !isListEmpty
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                //retryButton.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.characters.collect{
                    adapter.submitData(it)
                }
            }
        }

    }

    private fun FragmentHomeBinding.bindAdapter(charactersAdapter: CharactersAdapter){
        adapter = CharactersAdapter(getDeviceWidth())
        rvCharacters.adapter = charactersAdapter.withLoadStateHeaderAndFooter(
            header = CharactersLoadStateAdapter{ charactersAdapter.retry() },
            footer = CharactersLoadStateAdapter{ charactersAdapter.retry() }
        )
        rvCharacters.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        val paddingWidthInDp = 26 // width in dp
        val paddingWidthInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        Log.i("HomeFragment", displayMetrics.widthPixels.toString())
        return displayMetrics.widthPixels - paddingWidthInPx
    }

}