package com.mas.quranwords.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mas.quranwords.databinding.ItemWordBinding
import com.mas.quranwords.models.WordItem

class WordAdapter(
    private val onWordClicked: (WordItem) -> Unit
) : ListAdapter<WordItem, WordAdapter.WordViewHolder>(DiffCallback()) {

    class WordViewHolder(
        private val binding: ItemWordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            word: WordItem,
            onWordClicked: (WordItem) -> Unit
        ) {

            binding.wordTextView.text = word.word

            /*Glide.with(binding.root)
                .load(word.image)
                .into(binding.thumbnailImageView)*/

            binding.root.setOnClickListener {
                onWordClicked(word)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordViewHolder {

        val binding = ItemWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WordViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onWordClicked)
    }
}