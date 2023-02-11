package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robert.nganga.rickmorty.ui.epoxy.CharacterDetailsEpoxy
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.databinding.FragmentCharacterDetailsBinding
import com.robert.nganga.rickmorty.ui.MainActivity
import com.robert.nganga.rickmorty.ui.RickMortyViewModel
import com.robert.nganga.rickmorty.utils.Resource


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

        binding.epoxyRecyclerView.setController(epoxyController)

        viewModel.character.observe(viewLifecycleOwner) { response->
            when(response.status){
                Resource.Status.SUCCESS->{
                    epoxyController.character = response.data
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.LOADING -> {epoxyController.isLoading = true}
            }
        }

        viewModel.getCharacterById(args.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}