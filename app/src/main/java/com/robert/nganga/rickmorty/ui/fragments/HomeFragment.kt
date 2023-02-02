package com.robert.nganga.rickmorty.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.databinding.FragmentHomeBinding
import com.robert.nganga.rickmorty.ui.MainActivity
import com.robert.nganga.rickmorty.ui.RickMortyViewModel
import dagger.hilt.android.AndroidEntryPoint


class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: RickMortyViewModel
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

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_characterDetailsFragment)
        }
    }

}