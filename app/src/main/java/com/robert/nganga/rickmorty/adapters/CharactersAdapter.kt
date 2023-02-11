package com.robert.nganga.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.nganga.rickmorty.databinding.CharacterListItemBinding
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.model.Character

class CharactersAdapter(private val deviceWidth: Int) :
    PagingDataAdapter<CharacterResponse, CharactersAdapter.CharactersViewHolder>(REPO_COMPARATOR) {

    private var onItemClickListener: ((CharacterResponse)->Unit)? = null

    fun setOnItemClickListener(listener: (CharacterResponse)-> Unit){
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = getItem(position)
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(character!!) } }
        holder.setData(character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = CharacterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) SHOW_ITEM else LOADING_ITEM
    }

    inner class CharactersViewHolder(private val binding: CharacterListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(character: CharacterResponse?) {
            if (character != null) {
                val layoutParams = binding.imgCharacter.layoutParams
                layoutParams.width = deviceWidth / 2
                binding.imgCharacter.layoutParams = layoutParams
                Glide.with(binding.root).load(character.image).into(binding.imgCharacter)
                binding.tvListName.text = character.name
            }
        }

    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<CharacterResponse>() {
            override fun areItemsTheSame(oldItem: CharacterResponse, newItem: CharacterResponse): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CharacterResponse, newItem: CharacterResponse): Boolean =
                oldItem == newItem
        }

        private const val SHOW_ITEM = 0
        const val LOADING_ITEM = 1
    }
}