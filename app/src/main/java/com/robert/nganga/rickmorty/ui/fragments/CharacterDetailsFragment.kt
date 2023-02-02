package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.robert.nganga.rickmorty.CharacterDetailsEpoxy
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.databinding.FragmentCharacterDetailsBinding
import com.robert.nganga.rickmorty.ui.MainActivity
import com.robert.nganga.rickmorty.ui.RickMortyViewModel
import com.robert.nganga.rickmorty.utils.Resource
import dagger.hilt.android.AndroidEntryPoint



class CharacterDetailsFragment: Fragment(R.layout.fragment_character_details) {

    private  lateinit var viewModel: RickMortyViewModel
    private lateinit var epoxyController: CharacterDetailsEpoxy
    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        epoxyController = CharacterDetailsEpoxy()
        epoxyController.isLoading = true

        binding.epoxyRecyclerView.setController(epoxyController)

        viewModel.character.observe(viewLifecycleOwner) { response->
            when(response){
                is Resource.Success->{
                    epoxyController.characterResponse = response.data
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {}
            }

        }

        viewModel.getCharacterById(args.id)
    }

    private fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        val paddingWidthInDp = 26 // width in dp
        val paddingWidthInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            paddingWidthInDp.toFloat(), resources.displayMetrics).toInt()
        Log.i("CharacterDetailsFragment", displayMetrics.widthPixels.toString())
        return displayMetrics.widthPixels - paddingWidthInPx
    }
}