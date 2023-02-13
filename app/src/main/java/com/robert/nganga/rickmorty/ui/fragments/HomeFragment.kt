package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.robert.nganga.rickmorty.adapters.CharactersAdapter.Companion.LOADING_ITEM
import com.robert.nganga.rickmorty.adapters.CharactersLoadStateAdapter
import com.robert.nganga.rickmorty.databinding.FragmentHomeBinding
import com.robert.nganga.rickmorty.ui.MainActivity
import com.robert.nganga.rickmorty.ui.RickMortyViewModel
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
        adapter = CharactersAdapter(getImageWidth())
        binding.bindAdapter(adapter)
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("id", it.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_characterDetailsFragment, bundle)
        }

        binding.retryButton.setOnClickListener { adapter.retry() }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.Error && adapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty

                // Only show the list if refresh succeeds.
                binding.rvCharacters.isVisible = !isListEmpty

                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading && adapter.itemCount == 0

                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = loadState.refresh is LoadState.Error && adapter.itemCount == 0

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.characters.collect{
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentHomeBinding.bindAdapter(charactersAdapter: CharactersAdapter){
        val span = if (getDeviceWidth() > 700) 3 else 2
        val gridLayoutManager = GridLayoutManager(requireContext(), span, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (charactersAdapter.getItemViewType(position) == LOADING_ITEM)
                    1 else span
            }
        }
        rvCharacters.layoutManager = gridLayoutManager//GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        rvCharacters.adapter = charactersAdapter.withLoadStateFooter(
            footer = CharactersLoadStateAdapter{ charactersAdapter.retry() }
        )
    }

    private fun getImageWidth(): Int {
        val deviceWidth = getDeviceWidth()
        val paddingWidthInDp = 26 // padding in dp
        val paddingWidthInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        val width = deviceWidth - paddingWidthInPx
        return if (width > 700) {
            width / 3
        } else {
            width / 2
        }
    }

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }

}