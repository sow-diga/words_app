package com.mas.quranwords.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mas.quranwords.databinding.ItemWordBinding
import com.mas.quranwords.models.WordItem
import com.mas.quranwords.util.SurahNames

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
            word.surahNumber?.let {
                binding.surahName.text = SurahNames.getName(it)
            }

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