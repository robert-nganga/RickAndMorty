package com.robert.nganga.rickmorty

import android.util.Log
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.robert.nganga.rickmorty.databinding.ModelCharacterDataPointBinding
import com.robert.nganga.rickmorty.databinding.ModelCharacterImageBinding
import com.robert.nganga.rickmorty.databinding.ModelCharacterNameBinding
import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.ui.epoxy.LoadingEpoxyModel
import com.robert.nganga.rickmorty.ui.epoxy.ViewBindingKotlinModel


class CharacterDetailsEpoxy: EpoxyController() {
    
    var isLoading : Boolean = true
        set(value) {
            field = value
            if(field){
                requestModelBuild()
            }
        }
    
    var character: Character? = null
        set(value) {
            field = value
            if (field != null){
                isLoading = false
                requestModelBuild()
            }
        }
    override fun buildModels() {
        if(isLoading){
            LoadingEpoxyModel().id("loading").addTo(this)
            Log.i("CharacterDetails", "Now displaying the loading state")
            return
        }

        if(character == null){
            return
        }

        HeaderEpoxyModel(
            name = character!!.name,
            gender = character!!.gender,
            status = character!!.status
        ).id("header").addTo(this)

        ImageEpoxyModel(
            imageUrl = character!!.image
        ).id("image").addTo(this)

        character!!.origin?.let {
            DataPointEpoxy(
                title = "Origin",
                description = it.name
            ).id("origin").addTo(this)
        }

        DataPointEpoxy(
            title = "Species",
            description = character!!.species
        ).id("species").addTo(this)
        
    }
    
    data class HeaderEpoxyModel(
        val name: String,
        val gender: String,
        val status: String
    ): ViewBindingKotlinModel<ModelCharacterNameBinding>(R.layout.model_character_name){
        override fun ModelCharacterNameBinding.bind() {
            tvName.text = name
            tvStatus.text = status
            tvGender.text = gender
        }
    }

    data class ImageEpoxyModel(
        val imageUrl: String
    ): ViewBindingKotlinModel<ModelCharacterImageBinding>(R.layout.model_character_image){
        override fun ModelCharacterImageBinding.bind() {
            Glide.with(root).load(imageUrl).into(imgCharacter)
        }
    }

    data class DataPointEpoxy(
        val title: String,
        val description: String
    ): ViewBindingKotlinModel<ModelCharacterDataPointBinding>(R.layout.model_character_data_point){
        override fun ModelCharacterDataPointBinding.bind() {
            tvTitle.text = title
            Log.i("CharacterDetailsEpoxy", "Title text set with:: $title")
            tvDescription.text = description
        }

    }
}