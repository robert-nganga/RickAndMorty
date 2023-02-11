package com.robert.nganga.rickmorty.ui.epoxy

import android.util.Log
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.robert.nganga.rickmorty.R
import com.robert.nganga.rickmorty.databinding.*
import com.robert.nganga.rickmorty.model.Character
import com.robert.nganga.rickmorty.model.Episode


class CharacterDetailsEpoxy(): EpoxyController() {
    
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
            image = character!!.image,
            status = character!!.status
        ).id("header").addTo(this)

        PropertiesEpoxyModel(
            gender = character!!.gender,
            species = character!!.species,
            status = character!!.status
        ).id("properties").addTo(this)

        WhereAboutsEpoxy(
            location = character!!.location!!.name,
            origin = character!!.origin!!.name
        ).id("whereAbouts").addTo(this)

        EpisodeHeader(
            header = "EPISODES"
        ).id("episode_header").addTo(this)

        if(!character!!.episode.isNullOrEmpty()){
            val items = character!!.episode!!.map {
                Episodes(it).id(it.id)
            }
            CarouselModel_().models(items)
                    .id("episode_carousel")
                    .numViewsToShowOnScreen(1.0f)
                    .addTo(this)
        }
        
    }
    
    data class HeaderEpoxyModel(
        val name: String,
        val image: String,
        val status: String
    ): ViewBindingKotlinModel<EpoxyCharacterHeaderModelBinding>(R.layout.epoxy_character_header_model){
        override fun EpoxyCharacterHeaderModelBinding.bind() {

            when (status) {
                "Alive" -> { tvCharacterStatus.setTextColor(root.context.getColor(R.color.green)) }
                "Dead" -> { tvCharacterStatus.setTextColor(root.context.getColor(R.color.red)) }
                else -> { tvCharacterStatus.setTextColor(root.context.getColor(R.color.black)) }
            }

            tvCharacterName.text = name
            tvCharacterStatus.text = status
            Glide.with(root).load(image).into(imgCharacterImage)

        }
    }

    data class PropertiesEpoxyModel(
        val gender: String,
        val species: String,
        val status: String
    ): ViewBindingKotlinModel<EpoxyCharacterPropertiesModelBinding>(R.layout.epoxy_character_properties_model){
        override fun EpoxyCharacterPropertiesModelBinding.bind() {
            tvCharacterGender.text = gender
            tvCharacterSpecies.text = species
            tvCharacterStatus.text = status
        }
    }

    data class WhereAboutsEpoxy(
        val location: String,
        val origin: String
    ): ViewBindingKotlinModel<EpoxyCharacterWhereAboutsModelBinding>(R.layout.epoxy_character_where_abouts_model){
        override fun EpoxyCharacterWhereAboutsModelBinding.bind() {
            tvCharacterOrigin.text = origin
            tvCharacterLocation.text = location
            Log.i("CharacterDetailsEpoxy", "Origin text set with:: $origin")
        }

    }

    data class Episodes(
        val episode: Episode
    ): ViewBindingKotlinModel<EpoxyCharacterEpisodesModelBinding>(R.layout.epoxy_character_episodes_model){
        override fun EpoxyCharacterEpisodesModelBinding.bind() {
            tvEpisodeTitle.text = episode.name
            tvEpisode.text = episode.episode
            tvEpisodeAitDate.text = episode.air_date
        }

    }

    data class EpisodeHeader(
        val header:String
    ):ViewBindingKotlinModel<EpoxyCharacterEpisodeHeaderBinding>(R.layout.epoxy_character_episode_header){
        override fun EpoxyCharacterEpisodeHeaderBinding.bind() {
            episodeSection.text = header
        }
    }
}