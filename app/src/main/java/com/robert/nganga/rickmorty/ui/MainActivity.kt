package com.robert.nganga.rickmorty.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.robert.nganga.rickmorty.CharacterDetailsEpoxy
import com.robert.nganga.rickmorty.databinding.ActivityMainBinding
import com.robert.nganga.rickmorty.model.CharacterResponse
import com.robert.nganga.rickmorty.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: RickMortyViewModel by viewModels()
    private lateinit var epoxyController: CharacterDetailsEpoxy
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        epoxyController = CharacterDetailsEpoxy()
        epoxyController.isLoading = true

        binding.epoxyRecyclerView.setController(epoxyController)

        viewModel.character.observe(this) { response->
            when(response){
                is Resource.Success->{
                    epoxyController.characterResponse = response.data
                }
                is Resource.Error -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {}
            }

        }

        viewModel.getCharacterById(2)

    }
}