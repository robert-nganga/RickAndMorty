package com.robert.nganga.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robert.nganga.rickmorty.data.remote.response.CharacterResponse
import com.robert.nganga.rickmorty.databinding.SearchItemBinding


class SearchAdapter: PagingDataAdapter<CharacterResponse, SearchAdapter.SearchViewHolder>(SEARCH_COMPARATOR) {

    private var onItemClickListener: ((CharacterResponse)->Unit)? = null

    fun setOnItemClickListener(listener: (CharacterResponse)-> Unit){
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val character = getItem(position)
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(character!!) } }
        if (character != null) {
            holder.setData(character)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    inner class SearchViewHolder(private val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root){
        fun setData(character: CharacterResponse) {
            binding.apply {
                Glide.with(itemView).load(character.image).into(imgSearchCharacter)
                tvSearchName.text = character.name
                tvSearchSpecies.text = character.species.uppercase()
                tvSearchStatus.text = character.status
                tvSearchGender.text = character.gender
            }
        }
    }

    companion object {
        private val SEARCH_COMPARATOR = object : DiffUtil.ItemCallback<CharacterResponse>() {
            override fun areItemsTheSame(oldItem: CharacterResponse, newItem: CharacterResponse): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CharacterResponse, newItem: CharacterResponse): Boolean =
                oldItem == newItem
        }
    }

}