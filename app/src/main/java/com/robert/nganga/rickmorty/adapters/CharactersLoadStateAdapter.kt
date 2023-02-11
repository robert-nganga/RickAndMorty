package com.robert.nganga.rickmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robert.nganga.rickmorty.databinding.CharacterListFooterBinding

class CharactersLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<CharactersLoadStateAdapter.CharactersLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: CharactersLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CharactersLoadStateViewHolder {
        val binding = CharacterListFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersLoadStateViewHolder(binding, retry)
    }

    inner class CharactersLoadStateViewHolder(
        private val binding: CharacterListFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                val errorMsg = "Could not fetch characters at this time"
                binding.errorMsg.text = errorMsg
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }

    }
}